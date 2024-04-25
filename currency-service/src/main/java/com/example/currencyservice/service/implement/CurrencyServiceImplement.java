package com.example.currencyservice.service.implement;

import com.example.currencyservice.external.OpenExchangeRatesClient;
import com.example.currencyservice.model.CurrencyApiResponse;
import com.example.currencyservice.model.Currency;
import com.example.currencyservice.model.dto.CurrencyRequest;
import com.example.currencyservice.repository.CurrencyRepository;
import com.example.currencyservice.repository.CurrencyRequestRepository;
import com.example.currencyservice.service.CurrencyService;
import com.example.currencyservice.service.implement.utils.CurrencyServiceUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CurrencyServiceImplement implements CurrencyService {
    private CurrencyRepository currencyRepository;
    private CurrencyRequestRepository requestRepository;
    private final OpenExchangeRatesClient openExchangeRatesClient;

    private List<Currency> currencyList = new ArrayList<>();

    @Autowired
    public CurrencyServiceImplement(CurrencyRepository currencyRepository, CurrencyRequestRepository requestRepository, OpenExchangeRatesClient openExchangeRatesClient) {
        this.currencyRepository = currencyRepository;
        this.requestRepository = requestRepository;
        this.openExchangeRatesClient = openExchangeRatesClient;
    }

    @Override
    public Mono<List<Currency>> getCurrencyList(ZonedDateTime transaction_dateTime) {
        String currentDate_formatted = CurrencyServiceUtils.parseZoneDateTime(CurrencyServiceUtils.getCurrentDateTime());
        String transactionDate_formatted = CurrencyServiceUtils.parseZoneDateTime(transaction_dateTime);
        LocalDate parsedCurrentDate = LocalDate.parse(currentDate_formatted);
        LocalDate parsedTransactionDate = LocalDate.parse(transactionDate_formatted);

        CurrencyRequest pastCurrencyRequest = requestRepository.findByFormatted_timestamp(transactionDate_formatted);
        if (pastCurrencyRequest != null) {
            System.out.println("Get data from local db!!! (currencyList at:" + pastCurrencyRequest.getFormatted_timestamp() + ")");

            // Список валют взят из БД (запрос в openexchangerates.org/api/ НЕ делается)
            currencyList = currencyRepository.findAllByCurrencyRequestID(pastCurrencyRequest.getId());
            return checkForUnavailableRate(transaction_dateTime, currencyList);
        } else {
            return Mono.defer(() -> {
                if (parsedTransactionDate.isEqual(parsedCurrentDate)) {
                    return openExchangeRatesClient.getCurrencyList_Latest()
                            .map(response -> {
                                response.setTimestamp(currentDate_formatted);
                                return response;
                            })
                            .flatMap(response -> createCurrenciesFromResponse(Mono.just(response)))
                            .flatMap(currencyList -> checkForUnavailableRate(transaction_dateTime, currencyList));
                } else if (parsedTransactionDate.isBefore(parsedCurrentDate)) {
                    return openExchangeRatesClient.getCurrencyList_Historical(transactionDate_formatted)
                            .map(response -> {
                                response.setTimestamp(transactionDate_formatted);
                                return response;
                            })
                            .flatMap(response -> createCurrenciesFromResponse(Mono.just(response)))
                            .flatMap(currencyList -> checkForUnavailableRate(transaction_dateTime, currencyList));
                } else {
                    return Mono.error(new IllegalArgumentException("Transaction date cannot be in the future!!! (BACK-TO-THE-FUTURE)"));
                }
            });
        }
    }

    @Override
    public Mono<List<Currency>> createCurrenciesFromResponse(Mono<CurrencyApiResponse> responseMono) {
        return responseMono.flatMap(response -> {
            CurrencyRequest currencyRequest = getCurrencyRequest(response);
            requestRepository.save(currencyRequest);

            // Получаем список валют и сохраняем их в бд
            Map<String, BigDecimal> currencyRates = response.getRates();
            currencyList = new ArrayList<>();
            for (Map.Entry<String, BigDecimal> entry : currencyRates.entrySet()) {
                Currency currency = new Currency();
                currency.setCurrency_shortname(entry.getKey());
                currency.setRate_to_USD(entry.getValue());
                currencyList.add(currency);
            }
            currencyList.forEach(currency -> currency.setCurrencyRequest(currencyRequest));
            currencyRepository.saveAll(currencyList);
            return Mono.just(currencyList);
        });
    }

    @Override
    public CurrencyRequest getCurrencyRequest(CurrencyApiResponse response) {
        CurrencyRequest currencyRequest = new CurrencyRequest();
        currencyRequest.setBase(response.getBase());
        currencyRequest.setFormatted_timestamp(response.getTimestamp());
        return currencyRequest;
    }

    /* это немного условная реализация требования из ТЗ (пункт 3) ("использовать данные последнего закрытия (previous_close)")
     * потому что у меня free-plan от https://openexchangerates.org/api/,
     * но в документации говорится что для '/historical/*.json' + '/latest.json'
     * в качестве rates фактический берутся данные последнего закрытия,
     * а значит что если запрос (или запись из локальной БД) будет пустой
     * мы можем сделать еще один запрос (или взять данные из локальной БД)
     * на предыдущий день
     *      '/latest.json'=(The latest rates will always be the most up-to-date data available on your plan)
     *      '/historical/*.json'=(The historical rates returned are the last values we published for a given UTC day)  */
    @Override
    public Mono<List<Currency>> checkForUnavailableRate(ZonedDateTime transaction_dateTime, List<Currency> currencyList) {
        /* Если currencyList пустой, вызываем getCurrencyList на предыдущий день,
         * и так по рекурсии, пока не найдется доступный курс */
        if (currencyList.isEmpty()) {
            ZonedDateTime previousDate = transaction_dateTime.minusDays(1);
            return getCurrencyList(previousDate);
        } else {
            // Если currencyList не пустой, просто возвращаем значение
            return Mono.just(currencyList);
        }
    }



    @Override
    public List<CurrencyRequest> getPastRequestList() {
        return (List<CurrencyRequest>) requestRepository.findAll();
    }

    @Override
    public List<Currency> getPastCurrencyList() {
        return (List<Currency>) currencyRepository.findAll();
    }

}

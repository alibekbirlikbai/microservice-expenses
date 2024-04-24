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

    public CurrencyServiceImplement(OpenExchangeRatesClient openExchangeRatesClient) {
        this.openExchangeRatesClient = openExchangeRatesClient;
    }

    @Override
    public Mono<List<Currency>> getCurrencyList(ZonedDateTime transaction_dateTime) {
        String currentDate_formatted = CurrencyServiceUtils.parseZoneDateTime(CurrencyServiceUtils.getCurrentDateTime());
        String transactionDate_formatted = CurrencyServiceUtils.parseZoneDateTime(transaction_dateTime);
        LocalDate parsedCurrentDate = LocalDate.parse(currentDate_formatted);
        LocalDate parsedTransactionDate = LocalDate.parse(transactionDate_formatted);

        return Mono.defer(() -> {
            if (parsedTransactionDate.isEqual(parsedCurrentDate)) {
                return openExchangeRatesClient.getCurrencyList_Latest()
                        .map(response -> {
                            response.setTimestamp(currentDate_formatted);
                            return response;
                        })
                        .flatMap(response -> createCurrenciesFromResponse(Mono.just(response)));
            } else if (parsedTransactionDate.isBefore(parsedCurrentDate)) {
                return openExchangeRatesClient.getCurrencyList_Historical(transactionDate_formatted)
                        .map(response -> {
                            response.setTimestamp(transactionDate_formatted);
                            return response;
                        })
                        .flatMap(response -> createCurrenciesFromResponse(Mono.just(response)));
            } else {
                return Mono.error(new IllegalArgumentException("Transaction date cannot be in the future!!! (BACK-TO-THE-FUTURE)"));
            }
        });
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
//                currency.setCurrencyRequest(currencyRequest);
                currencyList.add(currency);
            }
            currencyList.forEach(currency -> currency.setCurrencyRequest(currencyRequest));
            currencyRepository.saveAll(currencyList);
            return Mono.just(currencyList);
        });
    }

//    @Override
//    public void saveRequestInDataBase(List<Currency> currencyList, CurrencyRequest currencyRequest) {
//        currencyRequest.setCurrencyList(currencyList);
//        currencyRepository.saveAll(currencyList);
//        requestRepository.save(currencyRequest);
//    }
//
//    @Override
//    public CurrencyRequest getOrCreateCurrencyRequest(CurrencyApiResponse response) {
//        String base = response.getBase();
//        String timestamp = response.getTimestamp();
//
//        // Проверяем существует ли уже объект CurrencyRequest с такими параметрами
//        CurrencyRequest currencyRequest = requestRepository.findByBaseAndFormatted_timestamp(base, timestamp);
//        if (currencyRequest == null) {
//            currencyRequest = new CurrencyRequest();
//            currencyRequest.setBase(base);
//            currencyRequest.setFormatted_timestamp(timestamp);
//        }
//
//        return currencyRequest;
//    }
//    @Override
    public CurrencyRequest getCurrencyRequest(CurrencyApiResponse response) {
        CurrencyRequest currencyRequest = new CurrencyRequest();
        currencyRequest.setBase(response.getBase());
        currencyRequest.setFormatted_timestamp(response.getTimestamp());
        return currencyRequest;
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

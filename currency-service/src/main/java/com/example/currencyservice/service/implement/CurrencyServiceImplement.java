package com.example.currencyservice.service.implement;

import com.example.currencyservice.external.OpenExchangeRatesClient;
import com.example.currencyservice.model.CurrencyApiResponse;
import com.example.currencyservice.model.Currency;
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
//    private CurrencyRepository currencyRepository;
//    private CurrencyApiResponseRepository responseRepository;
    private final OpenExchangeRatesClient openExchangeRatesClient;

    private List<Currency> currencyList = new ArrayList<>();
    private Mono<CurrencyApiResponse> apiResponseMono = Mono.empty();

    @Autowired
//    public CurrencyServiceImplement(CurrencyRepository currencyRepository, CurrencyApiResponseRepository responseRepository, OpenExchangeRatesClient openExchangeRatesClient) {
//        this.currencyRepository = currencyRepository;
//        this.responseRepository = responseRepository;
//        this.openExchangeRatesClient = openExchangeRatesClient;
//    }

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
            // Получаем список валют и сохраняем их в бд
            Map<String, BigDecimal> currencyRates = response.getRates();
            List<Currency> currencies = new ArrayList<>();
            for (Map.Entry<String, BigDecimal> entry : currencyRates.entrySet()) {
                Currency currency = new Currency();
                currency.setCurrency_shortname(entry.getKey());
                currency.setRate_to_USD(entry.getValue());
                currencies.add(currency);
            }
            return Mono.just(currencies);
        });
    }



//    @Override
//    public Mono<String> test() {
//        return openExchangeRatesClient.test()
//                .map(response -> {
//                    if (response.contains("Example Domain")) {
//                        // Request to example.com was successful
//                        return "Requested data from example.com";
//                    } else {
//                        // Request to example.com failed or returned unexpected content
//                        return "Failed to get data from example.com";
//                    }
//                });
//    }


}

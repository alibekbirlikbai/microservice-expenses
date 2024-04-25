package com.example.transactionservice.service.implement;

import com.example.transactionservice.external.CurrencyServiceClient;
import com.example.transactionservice.model.Currency;
import com.example.transactionservice.model.utils.CurrencyRequest;
import com.example.transactionservice.service.CurrencyService;
import jakarta.persistence.Id;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CurrencyServiceImplement implements CurrencyService {
    private final CurrencyServiceClient currencyServiceClient;

    @Autowired
    public CurrencyServiceImplement(CurrencyServiceClient currencyServiceClient) {
        this.currencyServiceClient = currencyServiceClient;
    }


    @Override
    public BigDecimal convertToUSD(String currency_shortname, BigDecimal transaction_sum, ZonedDateTime transaction_dateTime) {
        // Получаем список валют и их курсов (из API currency-service)
        List<Currency> currencyList = fetchCurrencyList(transaction_dateTime).block();
        Map<String, BigDecimal> currencyMap = getListOfCurrency(currencyList);

        // Проверяем, есть ли указанная валюта в списке
        if (!currencyMap.containsKey(currency_shortname)) {
            return BigDecimal.ZERO;
        }

        // Получаем курс валюты к USD
        BigDecimal exchangeRate = currencyMap.get(currency_shortname);

        // Конвертируем сумму в USD по курсу
        return transaction_sum.divide(exchangeRate, 2, RoundingMode.HALF_UP);
    }

    @Override
    public Map<String, BigDecimal> getListOfCurrency(List<Currency> currencyList) {
        Map<String, BigDecimal> currencyRatesMap = new HashMap<>();
        // Добавляем все валюты и их курсы к USD из списка currencyList
        for (Currency currency : currencyList) {
            currencyRatesMap.put(currency.getCurrency_shortname(), currency.getRate_to_USD());
        }

        return currencyRatesMap;
    }


    /* на этом методе происходит связь с другим сервисом проекта
     * (сервисом currency-service, через CurrencyServiceClient) */
    @Override
    public Mono<List<Currency>> fetchCurrencyList(ZonedDateTime transaction_dateTime) {
        List<Currency> currencyList = new ArrayList<>();
        return currencyServiceClient.getCurrencyList(transaction_dateTime)
                .doOnNext(response -> {
                    // Преобразование и добавление в currencyList
                    List<Currency> returnedCurrencies = response.stream()
                            .map(currency -> {
                                Currency newCurrency = new Currency();
                                newCurrency.setCurrency_shortname(currency.getCurrency_shortname());
                                newCurrency.setRate_to_USD(currency.getRate_to_USD());

                                CurrencyRequest newCurrencyRequest = new CurrencyRequest();
                                newCurrencyRequest.setBase(currency.getCurrencyRequest().getBase());
                                newCurrencyRequest.setFormatted_timestamp(currency.getCurrencyRequest().getFormatted_timestamp());

                                newCurrency.setCurrencyRequest(newCurrencyRequest);

                                return newCurrency;
                            })
                            .collect(Collectors.toList());

                    currencyList.addAll(returnedCurrencies);

                    System.out.println("- Data from currencyList (IN):" + currencyList);
                });
    }
}

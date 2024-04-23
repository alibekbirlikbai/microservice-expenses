package com.example.transactionservice.service.implement;

import com.example.transactionservice.model.Currency;
import com.example.transactionservice.service.CurrencyService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CurrencyServiceImplement implements CurrencyService {
    private Map<String, BigDecimal> currencyRates;

    @Override
    public Map<String, BigDecimal> getListOfCurrency(List<Currency> currencyList) {
        // Инициализируем карту валют и их курсов к USD
        currencyRates = new HashMap<>();

        // Добавляем все валюты и их курсы к USD из списка currencies
        for (Currency currency : currencyList) {
            currencyRates.put(currency.getCurrency_shortname(), currency.getRate_to_USD());
        }

        return currencyRates;
    }

    @Override
    public BigDecimal convertToUSD(BigDecimal transaction_sum, String currency_shortname) {
        // Получаем список валют и их курсов
        Map<String, BigDecimal> currencyMap = getListOfCurrency(fetchCurrencies());

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
    public List<Currency> fetchCurrencies() {
        // TEST currencyList
        List<Currency> currencyList = new ArrayList<>();

        // Добавляем значения для валют и их курсов в список
        currencyList.add(new Currency("USD", BigDecimal.ONE));
        currencyList.add(new Currency("KZT", BigDecimal.valueOf(400))); // Примерный курс KZT к USD
        currencyList.add(new Currency("RUB", BigDecimal.valueOf(70))); // Примерный курс RUB к USD

        return currencyList;
    }
}

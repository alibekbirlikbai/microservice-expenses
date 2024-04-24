package com.example.currencyservice.service.implement;

import com.example.currencyservice.external.OpenExchangeRatesClient;
import com.example.currencyservice.model.CurrencyApiResponse;
import com.example.currencyservice.model.Currency;
import com.example.currencyservice.repository.CurrencyRepo;
import com.example.currencyservice.service.CurrencyService;
import com.example.currencyservice.service.implement.utils.CurrencyServiceUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class CurrencyServiceImplement implements CurrencyService {
    private CurrencyRepo repository;
    private final OpenExchangeRatesClient openExchangeRatesClient;

    @Autowired
    public CurrencyServiceImplement(CurrencyRepo repository, OpenExchangeRatesClient openExchangeRatesClient) {
        this.repository = repository;
        this.openExchangeRatesClient = openExchangeRatesClient;
    }


    @Override
    public Mono<CurrencyApiResponse> getCurrencyList(ZonedDateTime dateTime) {
//        System.out.println("dateTime transaction: " + dateTime);
//        ZonedDateTime currentTime = CurrencyServiceUtils.getCurrentDateTime();
//        System.out.println("dateTime now: " + currentTime);
//
//        List<Currency> currencyList = new ArrayList<>();
//        Mono<CurrencyApiResponse> apiResponseMono = Mono.empty();
//
//
//        if (dateTime.isEqual(currentTime)) {
//            /* Если транзакция совершена сейчас
//             * т.е. у нее dateTime=ZonedDateTime.now()
//             * возвращаем курс валют на текущий момент '/latest.json' */
//            apiResponseMono = openExchangeRatesClient.getCurrencyList_Latest();
//            System.out.println("apiResponseMono = openExchangeRatesClient.getCurrencyList_Latest();");
//
//
//        } if (dateTime.isBefore(currentTime)) {
//            /* Если транзакция совершена в прошлом
//             * возвращаем курс валют на ту дату '/historical/*.json' */
//            apiResponseMono = openExchangeRatesClient.getCurrencyList_Historical(CurrencyServiceUtils.parseZoneDateTime(dateTime));
//            System.out.println("-> apiResponseMono = openExchangeRatesClient.getCurrencyList_Historical();");
//            System.out.println("formattedDate: " + CurrencyServiceUtils.parseZoneDateTime(dateTime));
//
//        } else {
//            /* Если транзакция совершена в будущем
//             * выбрасываем исключение */
//        }
//
//        System.out.println(apiResponseMono.toString());
//        System.out.println(apiResponseMono);
//        return apiResponseMono;

        return openExchangeRatesClient.getCurrencyList_Historical(CurrencyServiceUtils.parseZoneDateTime(dateTime));
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

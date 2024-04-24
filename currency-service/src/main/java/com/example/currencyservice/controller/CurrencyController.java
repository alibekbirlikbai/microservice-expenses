package com.example.currencyservice.controller;

import com.example.currencyservice.model.Currency;
import com.example.currencyservice.model.CurrencyApiResponse;
import com.example.currencyservice.service.CurrencyService;
import com.example.currencyservice.service.implement.CurrencyServiceImplement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.time.ZonedDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/currency-management")
public class CurrencyController {
    private CurrencyService service;

    @Autowired
    public CurrencyController(CurrencyServiceImplement service) {
        this.service = service;
    }

    @GetMapping("/{dateTime}")
    public Mono<CurrencyApiResponse> getCurrencyList(@PathVariable ZonedDateTime dateTime) {
        return service.getCurrencyList(dateTime);
    }

//    @GetMapping("/test")
//    public Mono<String> test() {
//        return service.test();
//    }
}

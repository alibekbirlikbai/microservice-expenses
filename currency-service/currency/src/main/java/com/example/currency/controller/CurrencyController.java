package com.example.currency.controller;


import com.example.currency.model.entity.CurrencyRequest;
import com.example.currency.model.entity.Currency;
import com.example.currency.service.CurrencyService;
import com.example.currency.service.impl.CurrencyServiceImplement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.time.ZonedDateTime;
import java.util.List;

@RestController
@RequestMapping("/currency-service/api/currencies")
public class CurrencyController {
    private final CurrencyService service;

    @Autowired
    public CurrencyController(CurrencyServiceImplement service) {
        this.service = service;
    }

    @GetMapping("/{dateTime}")
    public Mono<List<Currency>> getCurrencyList(@PathVariable("dateTime") ZonedDateTime dateTime) {
        return service.getCurrencyList(dateTime);
    }


    @GetMapping("/requests")
    public List<CurrencyRequest> getPastRequestList() {
        return service.getPastRequestList();
    }

    @GetMapping
    public List<Currency> getPastCurrencyList() {
        return service.getPastCurrencyList();
    }

}

package com.example.tgcurrencybotservice.controller;

import com.example.tgcurrencybotservice.exceptions.ServiceException;
import com.example.tgcurrencybotservice.model.Currency;
import com.example.tgcurrencybotservice.model.dto.CurrencyDto;
import com.example.tgcurrencybotservice.service.CurrencyService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.Optional;

@RestController
@RequestMapping("/api/currency")
@Validated
public class CurrencyController {

    @Autowired
    private CurrencyService currencyService;

    @PostMapping("/find")
    public Optional<Currency> findCurrencyByNameByDate(@Valid @RequestBody CurrencyDto currencyDto) {
        String currencyName = currencyDto.getCurrencyName();
        LocalDate date = currencyDto.getCurrencyDate();

        return currencyService.findCurrencyByNameByDate(currencyName, date);
    }

    @PostMapping("/create")
    public Currency createCurrency(@Valid @RequestBody CurrencyDto currencyDto) throws ServiceException {
        String currencyName = currencyDto.getCurrencyName();

        return currencyService.createCurrency(currencyName);
    }


}

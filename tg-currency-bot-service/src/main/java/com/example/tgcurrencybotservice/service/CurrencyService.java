package com.example.tgcurrencybotservice.service;

import com.example.tgcurrencybotservice.exceptions.ServiceException;
import com.example.tgcurrencybotservice.model.Currency;

import java.time.LocalDate;
import java.util.Optional;

public interface CurrencyService {
    Optional<Currency> findCurrencyByNameByDate(String currencyName, LocalDate date);

    Currency createCurrency(String currencyName) throws ServiceException;

    Currency saveCurrency(Currency createdCurrency);
}

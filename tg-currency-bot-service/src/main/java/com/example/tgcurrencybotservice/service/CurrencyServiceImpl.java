package com.example.tgcurrencybotservice.service;

import com.example.tgcurrencybotservice.exceptions.ServiceException;
import com.example.tgcurrencybotservice.model.Currency;
import com.example.tgcurrencybotservice.repository.CurrencyRepository;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;
import org.slf4j.Logger;

@Service
public class CurrencyServiceImpl implements CurrencyService {
    private static final Logger LOG = LoggerFactory.getLogger(CurrencyServiceImpl.class);

    @Autowired
    private CurrencyRepository currencyRepository;

    @Autowired
    private CbrRatesParseService cbrRatesParseService;

    @CachePut(value = "currency", key = "#currency.currencyName + '_' + #currency.currencyDate")
    public Currency saveCurrency(Currency currency) {
        Currency newCurrency = new Currency();

        LOG.info("Calling saveCurrency for currency: {} and date: {}",
                currency.getCurrencyName(), currency.getCurrencyDate());
        // Ищем по дате (без времени)
        LocalDate searchDate = currency.getCurrencyDate();
        String currencyName = currency.getCurrencyName();

        LOG.info("Calling saveCurrency.currencyRepository.findByCurrencyNameAndCurrencyDate(currencyName, date)...");
        Currency currencyDb = currencyRepository.findByCurrencyNameAndCurrencyDate(currencyName, searchDate);
        if(currencyDb == null) {
            Currency savedCurrency = currencyRepository.save(currency);
            LOG.info("Currency saved successfully: {}", savedCurrency);
            return newCurrency;
        } else {
            LOG.info("Currency already exists in DB: {}", currencyDb);
            return currencyDb;
        }
    }

    @Cacheable(value = "currency", key = "#currencyName + '_' + #date")
    public Optional<Currency> findCurrencyByNameByDate(String currencyName, LocalDate date) {
        Currency currencySearch = new Currency();

        LOG.info("Calling findCurrencyByNameByDate for currency: {} and date: {}",currencyName, date);

        currencySearch = currencyRepository.findByCurrencyNameAndCurrencyDate(currencyName, date);
        if (currencySearch == null) {
            LOG.info("Currency not found for name: {} and date: {}", currencyName, date);
            return Optional.empty();
        } else {
            LOG.info("Currency found: {}", currencySearch);
            return Optional.of(currencySearch);
        }
    }


    public Currency createCurrency(String currencyName) throws ServiceException {
        LOG.info("Calling createCurrency...");
        Currency currencyCreated = new Currency();
        currencyCreated.setCurrencyName(currencyName);
        currencyCreated.setCurrencyDate(LocalDate.now());

        switch (currencyName) {
            case "USD":
                currencyCreated.setCurrencyValue(cbrRatesParseService.getUSD());
                break;
            case "EUR":
                // код для евро
                break;
            default:
                // код для неизвестной валюты
                LOG.warn("Unknown currency: {}", currencyName);
                break;
        }
        return currencyCreated;

    }


    @Cacheable("currency")
    public Currency findCurrencyById(Long id) {
        LOG.info("Calling findCurrencyById...");
        Optional<Currency> currencyOptional = currencyRepository.findCurrencyById(id);
        return currencyOptional.orElse(null);
    }

    @CacheEvict(value = "currency", key = "#currency.id")
    public void deleteCurrency(Currency currency) {
        LOG.info("Calling deleteBook...");
        currencyRepository.delete(currency);
    }

}

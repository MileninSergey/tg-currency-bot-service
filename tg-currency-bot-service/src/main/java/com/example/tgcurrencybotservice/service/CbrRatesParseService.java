package com.example.tgcurrencybotservice.service;

import com.example.tgcurrencybotservice.exceptions.ServiceException;

public interface CbrRatesParseService {
    String getUSD() throws ServiceException;
}

package com.example.tgcurrencybotservice.repository;

import com.example.tgcurrencybotservice.model.Currency;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface CurrencyRepository extends JpaRepository<Currency, Long> {

    Optional<Currency> findCurrencyByCurrencyName(String currencyName);
    Optional<Currency> findCurrencyById(Long id);


    Currency findByCurrencyNameAndCurrencyDate(String attr0, LocalDate attr1);
}

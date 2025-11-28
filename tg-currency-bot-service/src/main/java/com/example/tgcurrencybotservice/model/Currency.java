package com.example.tgcurrencybotservice.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Table(name = "currency")
@Data
public class Currency {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // автогенерация ID
    private Long id;

    @Column(name = "currency_name")
    private String currencyName;

    @Column(name = "currency_value")
    private String currencyValue;

    @Column(name = "currency_date")
    private LocalDate currencyDate;

}

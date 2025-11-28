package com.example.tgcurrencybotservice.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;

@Data
public class CurrencyDto {

    @NotBlank(message = "Название валюты не может быть пустым")
    @Size(min = 3, max = 3, message = "Название валюты должно состоять из 3 символов")
    @Pattern(regexp = "^[A-Z]{3}$", message = "Название валюты должно состоять из 3 заглавных букв (например: USD, EUR, RUB)")
    private String currencyName;

    @PastOrPresent(message = "Дата валюты не может быть будущей")
    private LocalDate currencyDate;
}

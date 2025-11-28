package com.example.tgcurrencybotservice.model.dto;

import lombok.Data;

@Data
public class Usage {
    Integer promptTokens;
    Integer completionTokens;
    Integer totalTokens;
}

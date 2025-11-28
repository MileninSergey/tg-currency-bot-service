package com.example.tgcurrencybotservice.model.dto;

import lombok.Data;

import java.util.List;

@Data
public class ChatResponse {
    List<Choice> choices;
}

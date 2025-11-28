package com.example.tgcurrencybotservice.model.dto;

import lombok.Data;

import java.util.List;

@Data
public class ChatRequest {
    private String model;
    private List<Message> messages;
}

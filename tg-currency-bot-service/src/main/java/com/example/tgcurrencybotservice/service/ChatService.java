package com.example.tgcurrencybotservice.service;

import com.example.tgcurrencybotservice.model.dto.Message;

import java.util.List;

public interface ChatService {
    String getResponse(String chatId, String message);

    void clearHistory(String chatId);

    List<Message> clearHistoryRequest(String chatId);

    List<Message> getHistory(String chatId);

    Integer getCountMessages(String chatId);
}

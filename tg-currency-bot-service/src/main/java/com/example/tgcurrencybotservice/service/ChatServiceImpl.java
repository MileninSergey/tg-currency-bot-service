package com.example.tgcurrencybotservice.service;

import com.example.tgcurrencybotservice.model.dto.ChatRequest;
import com.example.tgcurrencybotservice.model.dto.ChatResponse;
import com.example.tgcurrencybotservice.model.dto.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService{
    @Value("${openrouter.api-key}")
    private String apiKey;

    @Value("${openrouter.api-url}")
    private String url;

    @Value("${openrouter.api-model}")
    private String model;

    // restTemplate
    private final RestTemplate restTemplate;

    Map<String, List<Message>> historyMap = new ConcurrentHashMap<>();

    //логика - методы для вызова чата
    public String getResponse(String chatId, String message) {

        Message messageObj = new Message();
        messageObj.setRole("user");
        messageObj.setContent(message);

        List<Message> messageHistoryByUserId = historyMap.computeIfAbsent(chatId, k->new ArrayList<>());
        messageHistoryByUserId.add(messageObj);

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(apiKey);
        headers.set("Content-Type", "application/json");

        ChatRequest chatRequest = new ChatRequest();
        chatRequest.setModel(model);
        chatRequest.setMessages(messageHistoryByUserId);

        HttpEntity<ChatRequest> request = new HttpEntity<>(chatRequest, headers);
        String erorrText = "Ошибка соединения";
        String responseText = "";

        try {
            // Отправляем запрос
            ResponseEntity<ChatResponse> response = restTemplate.postForEntity(url, request, ChatResponse.class);
            //должны set историю ссобщений с чатом ( user по chatId)
            System.out.println(response.getBody());
            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                responseText = response.getBody().getChoices().get(0).getMessage().getContent();

                //сохранять историю ссобщений в отдельном классе
                Message responseMsg = new Message();
                responseMsg.setContent(responseText);
                responseMsg.setRole("assistant");
                messageHistoryByUserId.add(responseMsg);

            }
        } catch (RestClientException e) {
            return erorrText;
        }
        return responseText;
    }

    //Методы для очистки истории по пользаку(по chatId)
    public void clearHistory(String chatId) {
        if (chatId != null) {
            historyMap.remove(chatId);
        }
    }

    //Методы для очистки истории по пользаку(по chatId) и вернуть все сообщения назад
    public List<Message> clearHistoryRequest(String chatId) {
        if (chatId == null) {
            return Collections.emptyList();
        }

        List<Message> removedMessages = historyMap.remove(chatId);
        return removedMessages != null ? removedMessages : Collections.emptyList();
    }

    public List<Message> getHistory(String chatId) {
        return historyMap.get(chatId);
    }

    public Integer getCountMessages(String chatId) {
        List<Message>  messages = historyMap.get(chatId);
        List<Message> messagesCount = messages.stream()
                .filter(m -> m.getRole().equals("user"))
                .collect(Collectors.toList());

        Integer count = messagesCount.size();
        return count;
    }
}

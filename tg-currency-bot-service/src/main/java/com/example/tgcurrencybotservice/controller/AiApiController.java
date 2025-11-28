package com.example.tgcurrencybotservice.controller;

import com.example.tgcurrencybotservice.model.dto.Message;
import com.example.tgcurrencybotservice.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ai")
public class AiApiController {

    private final ChatService chatService;

    @Autowired
    public AiApiController(ChatService chatService) {
        this.chatService = chatService;
    }

    @PostMapping("/get/qwen/{chatId}")
    public String getQwen(@RequestBody String message, @PathVariable String chatId) {
        return chatService.getResponse(chatId, message);
    }

    @DeleteMapping("/delete/qwen/{chatId}")
    public void clearHistory(@PathVariable String chatId) {
        chatService.clearHistory(chatId);
    }

    @DeleteMapping("/delete/qwen/chat/{chatId}")
    public List<Message> clearHistoryRequest(@PathVariable String chatId) {
        return chatService.clearHistoryRequest(chatId);
    }

    @GetMapping("/get/qwen/chat/{chatId}")
    public List<Message> getHistory(@PathVariable String chatId) {
        return chatService.getHistory(chatId);
    }

    @GetMapping("/get/qwen/count/{chatId}")
    public Integer getCountMessages(@PathVariable String chatId) {
        return chatService.getCountMessages(chatId);
    }


}

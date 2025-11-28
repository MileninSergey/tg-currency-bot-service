package com.example.tgcurrencybotservice.configuration;

import okhttp3.OkHttpClient;


import com.example.tgcurrencybotservice.bot.TgCurrencyBot;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@EnableCaching
@Configuration
public class CurrencyBotConfiguration {

    @Bean
    public TelegramBotsApi telegramBotsApi (TgCurrencyBot tgCurrencyBot) throws TelegramApiException {
        var api = new TelegramBotsApi(DefaultBotSession.class);
        api.registerBot(tgCurrencyBot);
        return api;
    }

    // бин OkHttpClient, чтобы иметь возможности выполнять http-запросы с помощью библиотеки okhttp
    @Bean
    public OkHttpClient okHttpClient() {
        return new OkHttpClient();
    }
}

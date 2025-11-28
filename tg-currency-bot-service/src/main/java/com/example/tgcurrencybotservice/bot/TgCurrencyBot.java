package com.example.tgcurrencybotservice.bot;

import com.example.tgcurrencybotservice.exceptions.ServiceException;
import com.example.tgcurrencybotservice.model.Currency;
import com.example.tgcurrencybotservice.service.CbrRatesParseService;
import com.example.tgcurrencybotservice.service.ChatService;
import com.example.tgcurrencybotservice.service.CurrencyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;


@Component
public class TgCurrencyBot extends TelegramLongPollingBot {

    private static final Logger LOG = LoggerFactory.getLogger(TgCurrencyBot.class);

    private static final String START = "/start";
    private static final String USD = "/usd";
    private static final String ANALYSIS = "/analysis";
    private static final String HELP = "/help";

    @Autowired
    private CurrencyService currencyService;

    @Autowired
    private ChatService chatService;

    private final String botUsername;

    public TgCurrencyBot(@Value("${bot.token}") String botToken, @Value("${bot.username}") String botUsername) {
        super(botToken);
        this.botUsername = botUsername;
    }

    @Value("${app.currency.usd-currency:USD}")
    String currencyName;

    //метод onUpdateReceived(Update update) вызывается всякий раз, когда пользователь отправляет в бот сообщение
    @Override
    public void onUpdateReceived(Update update) {
        if (!update.hasMessage()|| !update.getMessage().hasText()){
            return;
        }
        String message = update.getMessage().getText();
        Long chatId = update.getMessage().getChatId();
        switch (message) {
            case START -> {
                String userName = update.getMessage().getChat().getUserName();
                startCommand(chatId, userName);
            }
            case USD -> usdCommand(chatId);
            case ANALYSIS-> analysisCommand(chatId);
            case HELP -> helpCommand(chatId);
            default -> unknownCommand(chatId);
        }

    }

    //метод getBotUsername() должен возвращать название бота
    @Override
    public String getBotUsername() {
        return botUsername;
    }

    private void startCommand(Long chatId, String userName) {
        var text = """
                Добро пожаловать в бот, %s!
                
                Здесь Вы сможете узнать официальные курсы валют на сегодня, установленные ЦБ РФ.
                
                Для этого воспользуйтесь командами:
                /usd - курс доллара
                
                Дополнительные команды:
                /help - получение справки и список команд
                
                """;
        var formattedText = String.format(text, userName);
        sendMessage(chatId, formattedText);
    }

    private void usdCommand(Long chatId) {
        String formattedText;
        String currencyName = "USD";
        LocalDate nowTime = LocalDate.now();
        var usd = (String) null;
        Currency createdCurrency = new Currency();
        Currency savedCurrency = new Currency();
        try {
            Optional<Currency> getUsdCurrency = currencyService.findCurrencyByNameByDate(currencyName, nowTime);

            if(getUsdCurrency.isPresent()) {
                usd = getUsdCurrency.get().getCurrencyValue();
            } else {
                createdCurrency = currencyService.createCurrency(currencyName);
                savedCurrency = currencyService.saveCurrency(createdCurrency);
                usd = savedCurrency.getCurrencyValue();
            }

            var text = """
                          Курс доллара на %s составляет %s рублей";
                          
                          Дополнительные команды:
                          /help - получение справки
                          
                          /analysis - анализ курса валют
                          
                          """;
            formattedText = String.format(text, LocalDate.now(), usd);
        } catch (ServiceException e) {
            LOG.error("Ошибка получения курса доллара", e);
            formattedText = """ 
                                Не удалось получить текущий курс доллара. Попробуйте позже.
                                Дополнительные команды:
                                /help - получение справки
                             """;

        }
        sendMessage(chatId, formattedText);
    }

    private void sendMessage(Long chatId, String text) {
        var chatIdStr = String.valueOf(chatId);
        var sendMessage = new SendMessage(chatIdStr, text);
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            LOG.error("Ошибка отправки сообщения", e);
        }
    }

    private void helpCommand(Long chatId) {
        var text = """
                Справочная информация по боту
                
                Для получения текущих курсов валют воспользуйтесь командами:
                /usd - курс доллара
                """;
        sendMessage(chatId, text);
    }

    private void unknownCommand(Long chatId) {
        var text = "Не удалось распознать команду!";
        sendMessage(chatId, text);
    }

    private void analysisCommand(Long chatId) {
        String formattedText;
        String currencyName = "USD";
        Optional<Currency> getUsdCurrency = currencyService.findCurrencyByNameByDate(currencyName, LocalDate.now());

        String message = "Какой прогноз по текущему курсу "+currencyName+" на дату "+LocalDateTime.now()+" по ЦБ РФ "+ getUsdCurrency.get().getCurrencyValue();

        String returnMessage = chatService.getResponse(String.valueOf(chatId), message);

        var text = """
                      Анализ: на %s:
                      %s;
                      Дополнительные команды:
                      /help - получение справки
                      
                      /analysis - анализ курса валют
                      
                      """;
        formattedText = String.format(text, LocalDate.now(), returnMessage);
        sendMessage(chatId, formattedText);
    }

}

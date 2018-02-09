package com.gman.telegram.bot;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;

/**
 * Created by Anton Mikhaylov on 09.02.2018.
 */

@Service
public class Bot extends TelegramLongPollingBot {

    private final String TOKEN;
    private final String USERNAME;

    public Bot(@Value("${bot.token}") String token,
               @Value("${bot.username}") String username) {
        super();
        this.TOKEN = token;
        this.USERNAME = username;
    }



    @Override
    public String getBotToken() {
        return TOKEN;
    }

    @Override
    public String getBotUsername() {
        return USERNAME;
    }

    @Override
    public void onUpdateReceived(Update update) {
    }
}

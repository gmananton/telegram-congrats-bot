package com.gman.telegram.bot;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.ApiContext;
import org.telegram.telegrambots.api.methods.BotApiMethod;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;
import org.telegram.telegrambots.generics.BotOptions;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Anton Mikhaylov on 09.02.2018.
 */

@Slf4j
@Component
public class CongratsBot extends TelegramLongPollingBot {

    @Value("${bot.username}")
    private String BOT_NAME;

    @Value("${bot.token}")
    private String TOKEN;

    @PostConstruct
    public void init() {
        log.info("Bot {} initialized. Token: {}", BOT_NAME, TOKEN);
    }

    @Override
    public String getBotToken() {
        return TOKEN;
    }

    @Override
    public String getBotUsername() {
        return BOT_NAME;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            log.info("User sent text message: {}" , update);

            process(update);
        } else if (update.hasCallbackQuery()) {
            log.info("User sent callback: {}" , update);
            log.info(update.getCallbackQuery().getData());

        }
    }


    public void process(Update update) {
        final String smiling_face_with_heart_eyes =
                new String(Character.toChars(0x1F60D));
        final String winking_face = new String(Character.toChars(0x1F609));
        final String bouquet = new String(Character.toChars(0x1F490));
        final String party_popper = new String(Character.toChars(0x1F389));

        SendMessage answerMessage = null;
        String text = update.getMessage().getText();
        if ("/start".equalsIgnoreCase(text)) {
            answerMessage = new SendMessage();
            answerMessage.setText("<b>Привет!" + smiling_face_with_heart_eyes +
                    "\nВо-первых с днем рождения!"
                    + bouquet + bouquet + bouquet + party_popper
                    + " А во-вторых, ты готова поиграть в увлекательную викторину?</b>");
            answerMessage.setParseMode("HTML");
            answerMessage.setChatId(update.getMessage().getChatId());
            InlineKeyboardMarkup markup = keyboard(update);
            answerMessage.setReplyMarkup(markup);
            try {
                execute(answerMessage);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }

        }
    }

    private InlineKeyboardMarkup keyboard(Update update) {
        final InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
        keyboard.add(Arrays.asList(buttonMain()));
        markup.setKeyboard(keyboard);
        return markup;
    }

    private ReplyKeyboardMarkup keyboardReply(Update update) {
        ReplyKeyboardMarkup kb = new ReplyKeyboardMarkup();
        return kb;
    }

    private InlineKeyboardButton buttonMain() {
        final String OPEN_MAIN = "OM";
        final String winking_face = new String(Character.toChars(0x1F609));
        InlineKeyboardButton button = new InlineKeyboardButtonBuilder()
                .setText("Начать!" + winking_face)
                .setCallbackData(new Action(OPEN_MAIN).toString())
                .build();
        return button;
    }
}

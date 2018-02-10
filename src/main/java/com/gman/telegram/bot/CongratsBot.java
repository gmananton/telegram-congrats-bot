package com.gman.telegram.bot;

import com.gman.telegram.TextTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;

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
        if (isUpdateMsg(update)) {
            log.info("User sent text message: {}", update);
            process(update);
        } else if (isUpdateCallback(update)) {
            log.info("User sent callback: {}", update);
            log.info(update.getCallbackQuery().getData());

        }
    }


    public void process(Update update) {
        String text = update.getMessage().getText();
        SendMessage answer = new SendMessage();
        answer.setParseMode("HTML");
        answer.setChatId(update.getMessage().getChatId());
        InlineKeyboardMarkup markup = keyboard(update);
        answer.setReplyMarkup(markup);

        answer.setText(
                "/start".equalsIgnoreCase(text)
                        ? TextTemplate.HELLO_MSG
                        : TextTemplate.UNKNOWN_MSG
        );

        answer.setReplyMarkup(replyMarkup(update));

        try {
            execute(answer);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }




    private InlineKeyboardMarkup keyboard(Update update) {
        final InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
        keyboard.add(Arrays.asList(buttonMain()));
        markup.setKeyboard(keyboard);
        return markup;
    }

    private ReplyKeyboardMarkup replyMarkup(Update update) {
        ReplyKeyboardMarkup markup = new ReplyKeyboardMarkup();

        List<KeyboardRow> rows = new ArrayList<>(4);
        KeyboardRow row1 = new KeyboardRow();
        KeyboardRow row2 = new KeyboardRow();
        KeyboardRow row3 = new KeyboardRow();
        KeyboardRow row4 = new KeyboardRow();
        row1.add("var1");
        row2.add("var2");
        row3.add("var3");
        row4.add("var4");

        rows.addAll(Arrays.asList(row1, row2, row3, row4));

        markup.setKeyboard(rows);
        return markup;
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


    private boolean isUpdateMsg(Update update) {
        return update.hasMessage() && update.getMessage().hasText();
    }

    private boolean isUpdateCallback(Update update) {
        return update.hasCallbackQuery();
    }
}

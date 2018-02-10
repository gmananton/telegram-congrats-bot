package com.gman.telegram.bot;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gman.telegram.keyboard.KBBuilder;
import lombok.extern.slf4j.Slf4j;
import model.Pictures;
import model.Question;
import model.TextTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.methods.send.SendPhoto;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
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

    @Value("${chat.id.anton}")
    private String CHAT_ID;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private QuestionBuilder questionBuilder;

    @Autowired
    private KBBuilder keyboardBuilder;

    private List<Question> questions = new ArrayList<>();


    @PostConstruct
    public void init() throws Exception {
        questions = questionBuilder.createAll();
        log.info("Bot {} initialized. Token: {}", BOT_NAME, TOKEN);
        SendPhoto photoMsg = photoMessage(Pictures.PUSHEEN_CAKE, keyboardBuilder.getStartKB());
        sendPhoto(photoMsg);
        execute(textMessage(TextTemplate.UNKNOWN_MSG));
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
    public void onUpdateReceived(Update update)   {
        try {
            process(update);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void process(Update update) throws Exception {
        Message msg = update.getMessage();
        String text = msg.getText();

        if ("/start".equalsIgnoreCase(text)) {
            sendPhoto(photoMessage(Pictures.PUSHEEN_CAKE, keyboardBuilder.getStartKB()));
        }
    }

    private SendMessage textMessage(String text) {
        SendMessage msg = new SendMessage();
        msg.setChatId(CHAT_ID);
        msg.setParseMode("HTML");
        msg.setReplyMarkup(keyboardBuilder.getStartKB());
        msg.setText(text);
        return msg;
    }

    private SendPhoto photoMessage(String pictureID, ReplyKeyboardMarkup keyboard) {
        SendPhoto msg = new SendPhoto();
        msg.setChatId(CHAT_ID);
        msg.setPhoto(pictureID);
        msg.setCaption(TextTemplate.HELLO_MSG);
        msg.setReplyMarkup(keyboardBuilder.getStartKB());
        msg.setReplyMarkup(keyboard);
        return msg;
    }



    private boolean isPhotoMsg(Update update) {
        return update.hasMessage() && update.getMessage().hasPhoto();
    }

    private boolean isStickerMsg(Update update) {
        return update.hasMessage() && update.getMessage().hasDocument();
    }

    private boolean isTextMsg(Update update) {
        return update.hasMessage() && update.getMessage().hasText();
    }

}

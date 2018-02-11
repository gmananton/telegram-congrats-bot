package com.gman.telegram.bot;

import com.gman.telegram.data.Pictures;
import com.gman.telegram.data.BotTextTemplate;
import com.gman.telegram.data.UserTextTemplate;
import com.gman.telegram.keyboard.KBBuilder;
import com.gman.telegram.model.Question;
import com.gman.telegram.quest.AnswerValidator;
import com.gman.telegram.quest.QuestionProvider;
import lombok.extern.slf4j.Slf4j;
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
import java.util.Map;

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
    private QuestionProvider provider;

    @Autowired
    private KBBuilder keyboardBuilder;

    @Autowired
    private AnswerValidator validator;

    private List<Question> questions = new ArrayList<>();


    @PostConstruct
    public void init() throws Exception {
        log.info("Bot {} initialized. Token: {}", BOT_NAME, TOKEN);
        sendPhoto(getStartMessage());
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

        Map<Integer, Boolean> questState = provider.getQuestState();
        List<Question> questions = provider.getQuestions();

        Message msg = update.getMessage();
        String text = msg.getText();

        if (!validator.isAnswerSupported(text, questions)) {
            textMessage(BotTextTemplate.UNKNOWN_MSG);
            return;
        }

        if (UserTextTemplate.COMMAND_BEGIN.equalsIgnoreCase(text)) {
            sendPhoto(getStartMessage());
            return;
        }

        if (UserTextTemplate.GET_STARTED_MSG.equals(text)) {
            log.info("User started quest");

            Question question = questions.get(0);
            sendPhoto(photoMessage(
                    question.getPictureId(),
                    question.getText(),
                    keyboardBuilder.getKB(question)));

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

    private SendPhoto photoMessage(String pictureID, String text, ReplyKeyboardMarkup keyboard) {
        SendPhoto msg = new SendPhoto();
        msg.setChatId(CHAT_ID);
        msg.setPhoto(pictureID);
        msg.setCaption(text);
        msg.setReplyMarkup(keyboardBuilder.getStartKB());
        msg.setReplyMarkup(keyboard);
        return msg;
    }

    private SendPhoto getStartMessage() {
        return photoMessage(Pictures.PUSHEEN_CAKE, BotTextTemplate.HELLO_MSG, keyboardBuilder.getStartKB());
    }


}

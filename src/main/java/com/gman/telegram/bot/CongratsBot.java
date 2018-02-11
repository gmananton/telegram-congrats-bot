package com.gman.telegram.bot;

import com.gman.telegram.data.BotTextTemplate;
import com.gman.telegram.data.Pictures;
import com.gman.telegram.data.Stickers;
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
import org.telegram.telegrambots.api.methods.send.SendSticker;
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
    public void onUpdateReceived(Update update) {
        try {
            process(update);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void process(Update update) throws Exception {

        List<Question> questions = provider.getQuestions();

        Message msg = update.getMessage();
        String text = msg.getText();

        if (!validator.isAnswerSupported(text, questions)) {
            textMessage(BotTextTemplate.UNKNOWN_MSG, keyboardBuilder.startKB());
            return;
        }

        if (UserTextTemplate.COMMAND_BEGIN.equalsIgnoreCase(text)) {
            sendPhoto(getStartMessage());
            provider.init();
            return;
        }

        if (UserTextTemplate.GET_STARTED_MSG.equals(text)) {
            provider.init();
            sendPhoto(photoQuestion(provider.getNonAnsweredQuestion().get()));
            return;
        }

        if (UserTextTemplate.TRY_AGAIN_MSG.equals(text) || UserTextTemplate.CONTINUE_MSG.equals(text)) {
            if (provider.getNonAnsweredQuestion().isPresent()) {
                sendPhoto(photoQuestion(provider.getNonAnsweredQuestion().get()));
            } else {
                execute(textMessage("Вопросов больше не осталось", null)); //TODO
            }

            return;
        }

        if (validator.isAnswerSupported(text, questions)) {
            reactToAnswer(text);
            return;
        }


    }

    private void reactToAnswer(String text) throws Exception {

        Map<Integer, Boolean> questState = provider.getQuestState();

        //найти первый вопрос, на который еще не ответили
        int nonAnsweredId = provider.findFirstNonAnsweredId();


        if (provider.answerIsCorrect(text)) {
            questState.put(nonAnsweredId, true);
            sendSticker(stickerMessage(Stickers.CAT_SHERLOCK));
            execute(textMessage(BotTextTemplate.CORRECT_ANSWER, keyboardBuilder.continueKB()));
        } else {
            sendSticker(stickerMessage(Stickers.CAT_LAPTOP));
            execute(textMessage(BotTextTemplate.TRY_AGAIN, keyboardBuilder.tryAgainKB()));
        }
    }


    private SendMessage textMessage(String text, ReplyKeyboardMarkup keyboard) {
        SendMessage msg = new SendMessage();
        msg.setChatId(CHAT_ID);
        msg.setParseMode("HTML");
        msg.setReplyMarkup(keyboard);
        msg.setText(text);
        return msg;
    }

    private SendPhoto photoQuestion(Question question) {
        return photoMessage(question.getPictureId(), question.getText(), keyboardBuilder.keyboard(question));
    }

    private SendPhoto photoMessage(String pictureID, String text, ReplyKeyboardMarkup keyboard) {
        SendPhoto msg = new SendPhoto();
        msg.setChatId(CHAT_ID);
        msg.setPhoto(pictureID);
        msg.setCaption(text);
        msg.setReplyMarkup(keyboard);
        return msg;
    }

    private SendSticker stickerMessage(String pictureID) {
        SendSticker msg = new SendSticker();
        msg.setChatId(CHAT_ID);
        msg.setSticker(pictureID);
        return msg;
    }

    private SendPhoto getStartMessage() {
        return photoMessage(Pictures.PUSHEEN_CAKE, BotTextTemplate.HELLO_MSG, keyboardBuilder.startKB());
    }


}

package com.gman.telegram.bot;

import com.gman.telegram.keyboard.KBBuilder;
import com.gman.telegram.model.Question;
import com.gman.telegram.model.Reaction;
import com.gman.telegram.quest.AnswerRegistry;
import com.gman.telegram.quest.AnswerValidator;
import com.gman.telegram.quest.QuestionProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.api.methods.send.SendDocument;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.methods.send.SendPhoto;
import org.telegram.telegrambots.api.methods.send.SendSticker;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;

import java.util.List;
import java.util.Map;

import static com.gman.telegram.data.BotTextTemplate.HAPPY_BIRTHDAY;
import static com.gman.telegram.data.BotTextTemplate.HELLO_MSG;
import static com.gman.telegram.data.BotTextTemplate.UNKNOWN_MSG;
import static com.gman.telegram.data.Gifs.GIF_CORGI;
import static com.gman.telegram.data.Gifs.GIF_GIRL_CAKE;
import static com.gman.telegram.data.Gifs.GIF_SAMOYED;
import static com.gman.telegram.data.Gifs.GIF_SHEEP;
import static com.gman.telegram.data.Pictures.PUSHEEN_CAKE;
import static com.gman.telegram.data.Stickers.STICKER_CAT_UNICORN;
import static com.gman.telegram.data.UserTextTemplate.COMMAND_BEGIN;
import static com.gman.telegram.data.UserTextTemplate.CONTINUE_MSG;
import static com.gman.telegram.data.UserTextTemplate.GET_STARTED_MSG;
import static com.gman.telegram.data.UserTextTemplate.TRY_AGAIN_MSG;

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
    private AnswerRegistry answerRegistry;

    @Autowired
    private KBBuilder keyboardBuilder;

    @Autowired
    private AnswerValidator validator;


//    @PostConstruct
    @Scheduled(cron = "0 15 13 2 *")
    public void startMessaging() throws Exception {
        log.info("Bot {} initialized. Token: {}", BOT_NAME, TOKEN);
        sendPhoto(getStartMessage());
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
            textMessage(UNKNOWN_MSG, keyboardBuilder.startKB());
            return;
        }

        if (COMMAND_BEGIN.equalsIgnoreCase(text)) {
            sendPhoto(getStartMessage());
            provider.init();
            return;
        }

        if (GET_STARTED_MSG.equals(text)) {
            provider.init();
            sendPhoto(photoQuestion(provider.getNonAnsweredQuestion().get()));
            return;
        }

        if (TRY_AGAIN_MSG.equals(text) || CONTINUE_MSG.equals(text)) {
            if (provider.getNonAnsweredQuestion().isPresent()) {
                Question question = provider.getNonAnsweredQuestion().get();
                if (question.isClue()) {
                    execute(textMessage(question.getText(), null));
                } else {
                    sendPhoto(photoQuestion(question));
                }

            } else {
                sendDocument(gifMessage(GIF_SAMOYED));
                sendDocument(gifMessage(GIF_SHEEP));
                sendDocument(gifMessage(GIF_CORGI));
                sendSticker(stickerMessage(STICKER_CAT_UNICORN));
                execute(textMessage(HAPPY_BIRTHDAY, null));
                sendDocument(gifMessage(GIF_GIRL_CAKE));
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

        Reaction reaction = answerRegistry.findAnswerByText(text).getReaction();
        sendSticker(stickerMessage(reaction.getSticker()));

        if (provider.answerIsCorrect(text)) {
            questState.put(nonAnsweredId, true);
            execute(textMessage(reaction.getText(), keyboardBuilder.continueKB()));
        } else {
            execute(textMessage(reaction.getText(), keyboardBuilder.tryAgainKB()));
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

    private SendSticker stickerMessage(String pictureId) {
        SendSticker msg = new SendSticker();
        msg.setChatId(CHAT_ID);
        msg.setSticker(pictureId);
        return msg;
    }

    private SendDocument gifMessage(String pictureId) {
        SendDocument msg = new SendDocument();
        msg.setChatId(CHAT_ID);
        msg.setDocument(pictureId);
        return msg;
    }

    private SendPhoto getStartMessage() {
        return photoMessage(PUSHEEN_CAKE, HELLO_MSG, keyboardBuilder.startKB());
    }

    @Override
    public String getBotToken() {
        return TOKEN;
    }

    @Override
    public String getBotUsername() {
        return BOT_NAME;
    }

}

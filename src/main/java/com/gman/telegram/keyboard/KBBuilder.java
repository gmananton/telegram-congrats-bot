package com.gman.telegram.keyboard;

import com.gman.telegram.data.TextTemplate;
import com.gman.telegram.model.Question;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Anton Mikhaylov on 10.02.2018.
 */

@Component
public class KBBuilder {

    public ReplyKeyboardMarkup getKB(Question question) {

        ReplyKeyboardMarkup markup = new ReplyKeyboardMarkup();
        List<KeyboardRow> rows = new ArrayList<>(4);

        question.getAnswers().forEach(
                answer -> {
                    KeyboardRow row = new KeyboardRow();
                    row.add(answer.getText());
                    rows.add(row);
                });

        markup.setKeyboard(rows);
        return markup;
    }

    public ReplyKeyboardMarkup getStartKB() {
        ReplyKeyboardMarkup markup = new ReplyKeyboardMarkup();
        markup.setResizeKeyboard(true);
        List<KeyboardRow> rows = new ArrayList<>();
        KeyboardRow row = new KeyboardRow();
        row.add(TextTemplate.GET_STARTED_MSG);
        rows.add(row);
        markup.setKeyboard(rows);
        return markup;
    }
}

package com.gman.telegram.quest;

import com.gman.telegram.data.Pictures;
import com.gman.telegram.model.Answer;
import com.gman.telegram.model.Question;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Anton Mikhaylov on 10.02.2018.
 */
@Component
public class QuestionBuilder {

    public List<Question> createAll() {
        return Arrays.asList(quest_1(), quest_2(), quest_3());
    }

    public Question quest_1() {
        return Question.builder()
                .text("Что тут изображено?")
                .pictureId(Pictures.CORGI)
                .answers(Arrays.asList(
                                new Answer("Корги", false),
                                new Answer("Боченя", true),
                                new Answer("Пиченя", false),
                                new Answer("Тирства", false)))
                .build();
    }

    public Question quest_2() {
        return Question.builder()
                .text("Что это за вершина?")
                .pictureId(Pictures.EVEREST)
                .answers(Arrays.asList(
                        new Answer("Эверест", true),
                        new Answer("Монблан", false),
                        new Answer("Маттерхорн", false),
                        new Answer("Сорочаны", false)))
                .build();
    }

    public Question quest_3() {
        return Question.builder()
                .text("Что тут произошло?")
                .pictureId(Pictures.SNOWBOARD)
                .answers(Arrays.asList(
                        new Answer("Немножко кото-кот, чо =)", false),
                        new Answer("Смотри как умею!", false),
                        new Answer("Так и было задумано", true),
                        new Answer("Это паудер, детка!", false)))
                .build();
    }
}

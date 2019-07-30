package com.gman.telegram.quest;

import com.gman.telegram.model.Answer;
import com.gman.telegram.model.Reaction;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.gman.telegram.data.BotTextTemplate.TRY_AGAIN;
import static com.gman.telegram.data.Stickers.*;

/**
 * Created by Anton Mikhaylov on 11.02.2018.
 */

@Component
public class AnswerRegistry {

    // Связка номера вопроса и ответов
    private final Map<Integer, List<Answer>> registry = new HashMap<>();

    public List<Answer> get(int questNumber) {
        return Collections.unmodifiableList(registry.get(questNumber));
    }

    public Answer findAnswerByText(String text) {
        return registry.values().stream()
                .flatMap(List::stream)
                .filter(answer -> answer.getText().equalsIgnoreCase(text))
                .findFirst()
                .get();
    }

    @PostConstruct
    public void init() {

        registry.put(1, Arrays.asList(
                new Answer("Чук и Гек", false, incorrectReaction()),
                new Answer("Том и Ям", false, incorrectReaction()),
                new Answer("Котя и Кротя", false, incorrectReaction()),
                new Answer("Джек и Денилс", true, new Reaction(STICKER_HUGS, "Дооо!"))));

        registry.put(2, Arrays.asList(
                new Answer("31", true, new Reaction(STICKER_CAT_SHERLOCK, "Да ты прям Шерлок!"))));

        registry.put(3, Arrays.asList(
                new Answer("Селфи-палка", false, incorrectReaction()),
                new Answer("Профессиональная камера", false, incorrectReaction()),
                new Answer("Хороший телефон", true, new Reaction(STICKER_CAT_BOOK, "Он самый!")),
                new Answer("Ноутбук для работы", false, incorrectReaction())));


        registry.put(4, Arrays.asList(
                new Answer("Федя", false, incorrectReaction()),
                new Answer("Кеша", true, new Reaction(STICKER_CAT_PIANO, "Конечно!")),
                new Answer("Гриша", false, incorrectReaction()),
                new Answer("Слуга народа", false, incorrectReaction())));

        registry.put(5, Arrays.asList(
                new Answer("Хома", true, new Reaction(STICKER_MOOSE, "Верно!"))));
    }


    private Reaction incorrectReaction() {
        return new Reaction(STICKER_CAT_LAPTOP, TRY_AGAIN);
    }


}

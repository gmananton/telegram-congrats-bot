package com.gman.telegram.quest;

import com.gman.telegram.data.Pictures;
import com.gman.telegram.model.Answer;
import com.gman.telegram.model.Question;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created by Anton Mikhaylov on 10.02.2018.
 */
@Slf4j
@Component
public class QuestionProvider {

    /**
     * связка id вопроса и состояния "решен/не решен"
     * по команде /start происходит сброс состояния
     * */
    @Getter
    private Map<Integer, Boolean> questState = new LinkedHashMap<>();

    @Getter
    private List<Question> questions;

    @PostConstruct
    public void init() {
        questions = createAll();
        for (Question q : questions) {
            // Collectors.toMap - почему-то не работет
            questState.put(q.getId(), false);
        }
        log.info("QuestionProvider initialized");
    }

    public List<Question> createAll() {
        return Arrays.asList(quest_1(), quest_2(), quest_3());
    }


    public boolean answerIsCorrect(String answer) {
        return questions.stream()
                .map(Question::getAnswers)
                .flatMap(List::stream)
                .filter(Answer::isCorrect)
                .map(Answer::getText)
                .collect(Collectors.toList())
                .contains(answer);
    }

    // Находит первый id вопроса, на который еще не ответили
    public int findFirstNonAnsweredId() {
        Optional<Map.Entry<Integer, Boolean>> opt = questState.entrySet().stream()
                .filter(entry -> entry.getValue().equals(false))
                .findFirst();

        return opt.isPresent() ? opt.get().getKey() : -1;
    }


    public Optional<Question> getNonAnsweredQuestion() {
        return questions.stream()
                .filter(question -> question.getId().equals(findFirstNonAnsweredId()))
                .findFirst();
    }

    public Question quest_1() {
        return Question.builder()
                .id(0)
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
                .id(1)
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
                .id(2)
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

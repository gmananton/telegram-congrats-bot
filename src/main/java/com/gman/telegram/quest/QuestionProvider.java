package com.gman.telegram.quest;

import com.gman.telegram.data.Pictures;
import com.gman.telegram.model.Answer;
import com.gman.telegram.model.Question;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private AnswerRegistry answerRegistry;

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
        return Arrays.asList(
                quest_1(), clue_1(),
                quest_2(), clue_2(),
                quest_3(), clue_3());
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
                .answers(answerRegistry.get(0))
                .build();
    }

    public Question clue_1() {
        return Question.builder()
                .id(1)
                .text("<i>Она</i> защищает тебя от солнца и ветра во время катания.\nПопробуй найти <i>её</i> " +
                        "и внутри ты обнаружишь первую подсказку с кодом, который надо будет ввести в поле ответа.")
                .answers(answerRegistry.get(1))
                .clue(true)
                .build();
    }

    public Question quest_2() {
        return Question.builder()
                .id(2)
                .text("Что это за вершина?")
                .pictureId(Pictures.EVEREST)
                .answers(answerRegistry.get(2))
                .build();
    }

    public Question clue_2() {
        return Question.builder()
                .id(3)
                .text("Отлично! Я вижу, ты отгадала загадку. А вот следующая подсказка:\n" +
                        "Благодаря <i>им</i> мы познакомились")
                .answers(answerRegistry.get(3))
                .clue(true)
                .build();
    }



    public Question quest_3() {
        return Question.builder()
                .id(4)
                .text("Что тут произошло?")
                .pictureId(Pictures.SNOWBOARD)
                .answers(answerRegistry.get(4))
                .build();
    }

    public Question clue_3() {
        return Question.builder()
                .id(5)
                .text("Ага, значит, ты обнаружила первый сюрприз :) Но пока не открывай его - это еще не всё. " +
                        "Вот еще одна загадка.\n" +
                        "Это твой постоянный спутник во время путешествий и катания")
                .answers(answerRegistry.get(5))
                .clue(true)
                .build();
    }
}

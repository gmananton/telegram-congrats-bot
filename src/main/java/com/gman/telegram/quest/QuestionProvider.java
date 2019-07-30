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

import static com.gman.telegram.model.Question.Type.PHOTO;
import static com.gman.telegram.model.Question.Type.TEXT;
import static com.gman.telegram.model.Question.Type.TEXT_WITH_VARIANTS;

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
        return Arrays.asList(q1(), q2(), q3(), q4(), q5());
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

    public Question q1() {
        return Question.builder()
                .id(1)
                .type(PHOTO)
                .text("Кто изображен на картинке?")
                .pictureId(Pictures.JACK_DANIELS)
                .answers(answerRegistry.get(1))
                .build();
    }

    public Question q2() {
        return Question.builder()
                .id(2)
                .type(TEXT)
                .text("Вот и первая загадка:\n" +
                        "Он помогает тебе узнать, какой сейчас день недели или дата.\n" +
                        "Попробуй найти на нем перевернутое значение, которое надо будет внести в поле ответа.")
                .answers(answerRegistry.get(2))
                .build();
    }

    public Question q3() {
        return Question.builder()
                .id(3)
                .type(TEXT_WITH_VARIANTS)
                .text("Что нужно для начинающего блогера, который решил вести свой блог, выкладывать много видео-обзоров и много редактировать?")
                .answers(answerRegistry.get(3))
                .build();
    }

    public Question q4() {
        return Question.builder()
                .id(4)
                .type(TEXT_WITH_VARIANTS)
                .text("Как зовут робота, который живет с тобой?")
                .answers(answerRegistry.get(4))
                .build();
    }


    public Question q5() {
        return Question.builder()
                .id(5)
                .type(TEXT)
                .text("Теперь, когда ты догадалась о чем идет речь, попробуй найти <i>под ним</i> записку со словом, которое надо будет внести в поле ответа.")
                .answers(answerRegistry.get(5))
                .build();
    }
}

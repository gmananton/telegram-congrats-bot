package com.gman.telegram.quest;

import com.gman.telegram.data.BotTextTemplate;
import com.gman.telegram.model.Question;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;


/**
 * Created by Anton Mikhaylov on 11.02.2018.
 */
public class AnswerValidatorTest {

    @Test
    public void testAnswerIsSupported() {
        List<Question> questions = new QuestionProvider().createAll();
        AnswerValidator validator = new AnswerValidator();

        List<String> supportedAnswers = Arrays.asList(
                "Корги",
                "Боченя",
                "Пиченя",
                "Тирства",
                "Эверест",
                "Монблан",
                "Маттерхорн",
                "Сорочаны",
                "Немножко кото-кот, чо =)",
                "Смотри как умею!",
                "Так и было задумано",
                "Это паудер, детка!"
        );

        for (String answer : supportedAnswers) {
            assertTrue(validator.isAnswerSupported(answer, questions));
        }

        assertTrue(validator.isAnswerSupported(BotTextTemplate.COMMAND_BEGIN, questions));
        assertTrue(validator.isAnswerSupported(BotTextTemplate.GET_STARTED_MSG, questions));
        assertFalse(validator.isAnswerSupported("Some custom random user answer", questions));


    }
}
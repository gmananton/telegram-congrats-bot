package com.gman.telegram.data;

import static com.gman.telegram.data.Emojis.CONFUSED_FACE;
import static com.gman.telegram.data.Emojis.GRIMACING_FACE;
import static com.gman.telegram.data.Emojis.GRINNING_CAT;
import static com.gman.telegram.data.Emojis.PRESENT;
import static com.gman.telegram.data.Emojis.SMILING_CAT;

/**
 * Created by Anton Mikhaylov on 10.02.2018.
 * https://apps.timwhitlock.info/emoji/tables/unicode
 */
public class BotTextTemplate {

    public static final String HELLO_MSG =
            "Привет! Я поздравленческий кот!" + SMILING_CAT +
            "\nДавай сыграем в небольшую игру. За каждый верный ответ ты получишь подсказку, " +
            "которая приблизит тебя к победе." +
            "\nА в конце тебя ожидает сюрприз!" + GRINNING_CAT + PRESENT;

    public static final String UNKNOWN_MSG = "Извини, я не могу разобрать твой ответ... " + CONFUSED_FACE + "\nПопробуй еще раз или нажми /start, чтобы начать заново";
    public static final String TRY_AGAIN = "Упс, что-то не то..." + GRIMACING_FACE + "\nПопробуй еще раз";
    public static final String CORRECT_ANSWER = "Отлично!" + SMILING_CAT;


}

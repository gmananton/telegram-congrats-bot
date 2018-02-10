package model;

/**
 * Created by Anton Mikhaylov on 10.02.2018.
 * https://apps.timwhitlock.info/emoji/tables/unicode
 */
public class TextTemplate {
    public static final String WINKING_FACE = new String(Character.toChars(0x1F609));
    public static final String SMILING_CAT = new String(Character.toChars(0x1F63A));
    public static final String GRINNING_CAT = new String(Character.toChars(0x1F638));
    public static final String PRESENT = new String(Character.toChars(0x1F381));
    public static final String CONFUSED_FACE = new String(Character.toChars(0x1F615));


    public static final String GET_STARTED_MSG = "Начать!" + WINKING_FACE;

    public static final String HELLO_MSG =
            "Привет! Я поздравленческий кот!" + SMILING_CAT +
            "\nДавай сыграем в небольшую игру. За каждый верный ответ ты получишь подсказку, " +
            "которая приблизит тебя к победе." +
            "\nА в конце тебя ожидает сюрприз!" + GRINNING_CAT + PRESENT;

    public static final String UNKNOWN_MSG = "Извини, я не могу разобрать твой ответ... " + CONFUSED_FACE + "\nПопробуй еще раз или нажми /start, чтобы начать заново";


}

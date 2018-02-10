package com.gman.telegram.bot;

import org.telegram.telegrambots.api.objects.replykeyboard.buttons.InlineKeyboardButton;

/**
 * Created by Anton Mikhaylov on 09.02.2018.
 */
public class InlineKeyboardButtonBuilder {
    private final InlineKeyboardButton button;

    public InlineKeyboardButtonBuilder(){
        this.button = new InlineKeyboardButton();
    }


    public InlineKeyboardButtonBuilder setText(String text){
        button.setText(text);
        return this;
    }

    public InlineKeyboardButtonBuilder setCallbackData(String callbackData){
        button.setCallbackData(callbackData);
        return this;
    }

    public InlineKeyboardButton build(){
        return button;
    }
}

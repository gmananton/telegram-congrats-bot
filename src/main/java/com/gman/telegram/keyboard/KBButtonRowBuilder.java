package com.gman.telegram.keyboard;

import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Anton Mikhaylov on 10.02.2018.
 */
public class KBButtonRowBuilder {

    private final List<KeyboardRow> row;

    public KBButtonRowBuilder() {
        this.row = new ArrayList<>();
    }
}

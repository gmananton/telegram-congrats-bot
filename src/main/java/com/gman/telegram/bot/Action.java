package com.gman.telegram.bot;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

/**
 * Created by Anton Mikhaylov on 09.02.2018.
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Action {
    protected String name = "";
    protected String id = "";
    protected String value = "";

    public Action(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String value) {
        this.name = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}

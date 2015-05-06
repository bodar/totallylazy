package com.googlecode.totallylazy.template.ast;

import com.googlecode.totallylazy.Value;
import com.googlecode.totallylazy.template.Renderer;

import java.util.Map;


public class Text implements Value<String>, Node {
    private final String value;

    public Text(String value) {
        this.value = value;
    }


    public String value() {
        return value;
    }
}

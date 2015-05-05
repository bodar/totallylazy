package com.googlecode.totallylazy.template.ast;

import com.googlecode.totallylazy.Value;
import com.googlecode.totallylazy.template.Funclate;
import com.googlecode.totallylazy.template.Renderer;

import java.util.Map;


public class Attribute implements Value<String>, Node {
    private final String value;

    public Attribute(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }

    @Override
    public String toString() {
        return value;
    }
}

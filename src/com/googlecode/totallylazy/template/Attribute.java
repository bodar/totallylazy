package com.googlecode.totallylazy.template;

import com.googlecode.totallylazy.Value;

import java.util.Map;


public class Attribute implements Value<String>, Renderer<Map<String, Object>> {
    private final String value;
    private final Funclate funclate;

    public Attribute(String value, Funclate funclate) {
        this.value = value;
        this.funclate = funclate;
    }

    public String value() {
        return value;
    }

    @Override
    public <A extends Appendable> A render(Map<String, Object> instance, A appendable) throws Exception {
        return funclate.render(instance.get(value), appendable);
    }

    @Override
    public String toString() {
        return value;
    }
}

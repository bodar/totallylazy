package com.googlecode.totallylazy.template.ast;

import java.util.Map;

public class TemplateCall implements Expression {
    private final String name;
    private final Map<String, Object> arguments;

    public TemplateCall(final String name, final Map<String, Object> arguments) {
        this.name = name;
        this.arguments = arguments;
    }

    public String name() {
        return name;
    }

    public Map<String, Object> arguments() {
        return arguments;
    }
}

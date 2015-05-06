package com.googlecode.totallylazy.template.ast;

import java.util.Map;

public class TemplateCall implements Node {
    private final String name;
    private final Map<String, Node> arguments;

    public TemplateCall(final String name, final Map<String, Node> arguments) {
        this.name = name;
        this.arguments = arguments;
    }

    public String name() {
        return name;
    }

    public Map<String, Node> arguments() {
        return arguments;
    }
}

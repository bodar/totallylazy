package com.googlecode.totallylazy.template.ast;

import java.util.List;

import static com.googlecode.totallylazy.Sequences.sequence;

public class AnonymousTemplate implements Expression {
    private final List<String> paramaeterNames;
    private final List<Object> template;

    public AnonymousTemplate(List<String> paramaeterNames, List<Object> template) {
        this.paramaeterNames = paramaeterNames;
        this.template = template;
    }

    public List<String> paramaeterNames() {
        return paramaeterNames;
    }

    public List<Object> template() {
        return template;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "( " +
                sequence(paramaeterNames) +
                " | " + sequence(template).toString("") +
                ")";
    }
}

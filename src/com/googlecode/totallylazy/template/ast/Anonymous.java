package com.googlecode.totallylazy.template.ast;

import java.util.List;

import static com.googlecode.totallylazy.Sequences.sequence;

public class Anonymous implements Expression {
    private final List<String> paramaeterNames;
    private final List<Expression> template;

    public Anonymous(List<String> paramaeterNames, List<Expression> template) {
        this.paramaeterNames = paramaeterNames;
        this.template = template;
    }

    public List<String> paramaeterNames() {
        return paramaeterNames;
    }

    public List<Expression> template() {
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

package com.googlecode.totallylazy.template.ast;

import com.googlecode.totallylazy.Eq;
import com.googlecode.totallylazy.annotations.multimethod;

import java.util.List;
import java.util.Objects;

import static com.googlecode.totallylazy.Sequences.sequence;

public class Anonymous extends Eq implements Expression {
    private final List<String> paramaeterNames;
    private final List<Expression> template;

    private Anonymous(List<String> paramaeterNames, List<Expression> template) {
        this.paramaeterNames = paramaeterNames;
        this.template = template;
    }

    public static Anonymous anonymous(List<String> paramaeterNames, List<Expression> template) {
        return new Anonymous(paramaeterNames, template);
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

    @multimethod
    public boolean equals(Anonymous other){
        return other.paramaeterNames.equals(paramaeterNames) && other.template.equals(template);
    }

    @Override
    public int hashCode() {
        return Objects.hash(paramaeterNames, template);
    }
}

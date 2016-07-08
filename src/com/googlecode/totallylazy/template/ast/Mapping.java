package com.googlecode.totallylazy.template.ast;

import com.googlecode.totallylazy.Eq;
import com.googlecode.totallylazy.annotations.multimethod;

import java.util.Objects;

public class Mapping extends Eq implements Expression {
    private final Attribute attribute;
    private final Anonymous expression;

    private Mapping(Attribute attribute, Anonymous expression) {
        this.attribute = attribute;
        this.expression = expression;
    }

    public static Mapping mapping(Attribute attribute, Anonymous expression) {return new Mapping(attribute, expression);}

    public Attribute attribute() {
        return attribute;
    }

    public Anonymous expression() {
        return expression;
    }

    @multimethod
    public boolean equals(Mapping other) {
        return Objects.equals(attribute, other.attribute) &&
                Objects.equals(expression, other.expression);
    }

    @Override
    public int hashCode() {
        return Objects.hash(attribute, expression);
    }
}

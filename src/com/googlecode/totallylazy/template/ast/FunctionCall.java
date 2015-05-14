package com.googlecode.totallylazy.template.ast;

import com.googlecode.totallylazy.Eq;
import com.googlecode.totallylazy.annotations.multimethod;

import java.util.Objects;

import static com.googlecode.totallylazy.Sequences.sequence;
import static com.googlecode.totallylazy.Unchecked.cast;

public class FunctionCall extends Eq implements Expression {
    private final Expression name;
    private final Arguments<?> arguments;

    private FunctionCall(final Expression name, final Arguments<?> arguments) {
        this.name = name;
        this.arguments = arguments;
    }

    public static FunctionCall functionCall(final Expression name, final Arguments<?> arguments) {return new FunctionCall(name, arguments);}

    public Expression name() {
        return name;
    }

    public Arguments<?> arguments() {
        return cast(arguments);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" + name + '(' +
                sequence(arguments) + "))";
    }

    @multimethod
    public boolean equals(FunctionCall other){
        return other.arguments.equals(arguments) && other.name.equals(name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(arguments, name);
    }
}

package com.googlecode.totallylazy.template.ast;

import com.googlecode.totallylazy.Pair;
import com.googlecode.totallylazy.Value;

import java.util.Iterator;
import java.util.List;

import static com.googlecode.totallylazy.Sequences.sequence;

public class ImplicitArguments implements Arguments<Number>, Value<List<Expression>> {
    private final List<Expression> arguments;

    public ImplicitArguments(List<Expression> arguments) {
        this.arguments = arguments;
    }

    @Override
    public List<Expression> value() {
        return arguments;
    }

    @Override
    public Iterator<Pair<Number, Expression>> iterator() {
        return sequence(arguments).zipWithIndex().iterator();
    }
}

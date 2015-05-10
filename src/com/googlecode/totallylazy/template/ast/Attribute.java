package com.googlecode.totallylazy.template.ast;

import com.googlecode.totallylazy.Value;

import java.util.List;

import static com.googlecode.totallylazy.Arrays.list;
import static com.googlecode.totallylazy.Sequences.sequence;
import static com.googlecode.totallylazy.Unchecked.cast;


public class Attribute extends Value.Type<List<Expression>> implements Expression {
    private Attribute(List<Expression> value) {
        super(value);
    }

    @SafeVarargs
    public static Attribute attribute(Expression... value) {return new Attribute(list(value));}
    public static Attribute attribute(List<Expression> value) {return new Attribute(value);}
}

package com.googlecode.totallylazy.template.ast;

import com.googlecode.totallylazy.Value;

import java.util.List;

import static com.googlecode.totallylazy.Sequences.sequence;


public class Attribute implements Value<List<String>>, Expression {
    private final List<String> value;

    public Attribute(List<String> value) {
        this.value = value;
    }

    public List<String> value() {
        return value;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + sequence(value).toString("(" , ".", ")") ;
    }
}

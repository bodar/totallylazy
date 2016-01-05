package com.googlecode.totallylazy.parser;

import com.googlecode.totallylazy.Segment;

import java.util.concurrent.atomic.AtomicReference;

class ReferenceParser<T> implements Parser<T> {
    private final AtomicReference<Parser<T>> value = new AtomicReference<Parser<T>>();

    private ReferenceParser() {}

    public static <T> ReferenceParser<T> reference() {return new ReferenceParser<T>();}

    @Override
    public String toString() {
        return value.get().toString();
    }

    @Override
    public Result<T> parse(Segment<Character> characters) {
        return value.get().parse(characters);
    }

    public ReferenceParser<T> set(Parser<T> parse) {
        value.set(parse);
        return this;
    }

}
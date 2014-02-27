package com.googlecode.totallylazy.parser;

import com.googlecode.totallylazy.Segment;

import java.util.concurrent.atomic.AtomicReference;

// Use in Java 6, for Java 7+ use Parsers.lazy
public class ReferenceParser<T> extends Parser<T> {
    private final AtomicReference<Parse<T>> value = new AtomicReference<Parse<T>>();

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

    public ReferenceParser<T> set(Parse<T> parse) {
        value.set(parse);
        return this;
    }

}
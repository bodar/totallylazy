package com.googlecode.totallylazy.parser;

import com.googlecode.totallylazy.Segment;

import java.util.concurrent.atomic.AtomicReference;

public class Reference<T> extends Parser<T> {
    private final AtomicReference<Parse<T>> value = new AtomicReference<Parse<T>>();

    @Override
    public String toString() {
        return value.get().toString();
    }

    @Override
    public Result<T> parse(Segment<Character> characters) {
        return value.get().parse(characters);
    }

    public Reference<T> set(Parse<T> parse) {
        value.set(parse);
        return this;
    }

}

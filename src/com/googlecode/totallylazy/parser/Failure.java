package com.googlecode.totallylazy.parser;

import com.googlecode.totallylazy.Function;

import java.nio.CharBuffer;

public class Failure<A> implements Result<A> {
    private final String message;

    private Failure(String message) {
        this.message = message;
    }

    public static <A> Failure<A> failure(String message) {
        return new Failure<A>(message);
    }

    @Override
    public <B> Failure<B> map(Function<? super A, ? extends B> callable) {
        return failure(message);
    }

    @Override
    public CharBuffer remainder() {
        throw fail();
    }

    @Override
    public A value() {
        throw fail();
    }

    public String message() {
        return message;
    }

    private RuntimeException fail() {
        return new RuntimeException(message);
    }
}

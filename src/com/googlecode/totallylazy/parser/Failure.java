package com.googlecode.totallylazy.parser;

import com.googlecode.totallylazy.Callable1;

public class Failure<A> implements Result<A>{
    private final String message;

    private Failure(String message) {
        this.message = message;
    }

    public static <A> Failure<A> failure(String message) {
        return new Failure<A>(message);
    }

    @Override
    public <B> Failure<B> map(Callable1<? super A, ? extends B> callable) {
        return failure(message);
    }

    @Override
    public A value() {
        throw new RuntimeException(message);
    }

    public String message() {
        return message;
    }
}

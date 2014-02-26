package com.googlecode.totallylazy.parser;

import com.googlecode.totallylazy.Callable1;
import com.googlecode.totallylazy.Either;
import com.googlecode.totallylazy.Option;
import com.googlecode.totallylazy.Segment;

import static com.googlecode.totallylazy.Option.none;

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
    public Option<A> option() {
        return none();
    }

    @Override
    public Either<String, A> either() {
        return Either.left(message);
    }

    @Override
    public boolean success() {
        return false;
    }

    @Override
    public boolean failure() {
        return true;
    }

    @Override
    public Segment<Character> remainder() {
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

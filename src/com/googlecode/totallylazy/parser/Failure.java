package com.googlecode.totallylazy.parser;

import com.googlecode.totallylazy.Callable1;
import com.googlecode.totallylazy.Either;
import com.googlecode.totallylazy.Option;
import com.googlecode.totallylazy.Segment;

import static com.googlecode.totallylazy.Option.none;
import static com.googlecode.totallylazy.Unchecked.cast;

public class Failure<A> implements Result<A>{
    private final Object expected, actual;

    private Failure(Object expected, Object actual) {
        this.expected = expected;
        this.actual = actual;
    }

    public static <A> Failure<A> failure(Object expected, Object actual) {
        return new Failure<A>(expected, actual);
    }

    @Override
    public <B> Failure<B> map(Callable1<? super A, ? extends B> callable) {
        return cast(this);
    }

    @Override
    public Option<A> option() {
        return none();
    }

    @Override
    public Either<String, A> either() {
        return Either.left(message());
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

    @Override
    public String message() {
        return expected + " expected, " + actual + " encountered.";
    }

    private RuntimeException fail() {
        return new RuntimeException(message());
    }
}

package com.googlecode.totallylazy.parser;

import com.googlecode.totallylazy.Function1;
import com.googlecode.totallylazy.Either;
import com.googlecode.totallylazy.Option;
import com.googlecode.totallylazy.Segment;

import static com.googlecode.totallylazy.Option.none;
import static com.googlecode.totallylazy.Unchecked.cast;

public abstract class Failure<A> implements Result<A>{
    public static <A> Failure<A> failure(final Object expected, final Object actual) {
        return new Failure<A>() {
            @Override
            public String message() {
                return String.format("%s expected, %s encountered.", expected, actual);
            }
        };
    }

    public static <A> Failure<A> failure(final Object expected, final Exception actual) {
        return failure(expected, actual.getMessage());
    }

    @Override
    public <B> Failure<B> map(Function1<? super A, ? extends B> callable) {
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

    private RuntimeException fail() {
        return new RuntimeException(message());
    }

    @Override
    public String toString() {
        return "Failure(\"" + message() + "\")";
    }
}

package com.googlecode.totallylazy.parser;

import com.googlecode.totallylazy.functions.Function1;
import com.googlecode.totallylazy.Either;
import com.googlecode.totallylazy.functions.Functions;
import com.googlecode.totallylazy.Option;
import com.googlecode.totallylazy.Segment;

import java.util.NoSuchElementException;

import static com.googlecode.totallylazy.Option.some;

public interface Success<A> extends Result<A> {
    static <A> Success<A> success(final A value, final Segment<Character> remainder) {
        return new Success<A>() {
            @Override
            public Segment<Character> remainder() {
                return remainder;
            }

            @Override
            public A value() {
                return value;
            }

            @Override
            public String toString() {
                return "Success(" + value() + ")";
            }
        };
    }

    @Override
    default Object actual() {
        return value();
    }

    @Override
    default <S> Result<S> map(Function1<? super A, ? extends S> callable) {
        return success(Functions.call(callable, value()), remainder());
    }

    @Override
    default Option<A> option() {
        return some(value());
    }

    @Override
    default Either<String, A> either() {
        return Either.right(value());
    }

    @Override
    default boolean success() {
        return true;
    }

    @Override
    default boolean failure() {
        return false;
    }

    @Override
    default String message() {
        throw new NoSuchElementException();
    }

}

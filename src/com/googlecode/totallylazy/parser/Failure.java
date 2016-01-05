package com.googlecode.totallylazy.parser;

import com.googlecode.totallylazy.functions.Function1;
import com.googlecode.totallylazy.Either;
import com.googlecode.totallylazy.Option;
import com.googlecode.totallylazy.Segment;

import static com.googlecode.totallylazy.Option.none;
import static com.googlecode.totallylazy.Unchecked.cast;

public interface Failure<A> extends Result<A>{
    static <A> Failure<A> failure(final Object expected, final Object actual) {
        return new Failure<A>() {
            @Override
            public Object actual() {
                return actual;
            }

            @Override
            public String message() {
                return String.format("%s expected, %s encountered.", expected, actual);
            }

            @Override
            public String toString() {
                return "Failure(\"" + message() + "\")";
            }
        };
    }

    static <A> Failure<A> failure(final Object expected, final Exception actual) {
        return failure(expected, actual.getMessage());
    }

    @Override
    default <B> Failure<B> map(Function1<? super A, ? extends B> callable) {
        return cast(this);
    }

    @Override
    default Option<A> option() {
        return none();
    }

    @Override
    default Either<String, A> either() {
        return Either.left(message());
    }

    @Override
    default boolean success() {
        return false;
    }

    @Override
    default boolean failure() {
        return true;
    }

    @Override
    default Segment<Character> remainder() {
        throw exception();
    }

    @Override
    default A value() {
        throw exception();
    }

    default RuntimeException exception() {
        return new RuntimeException(message());
    }
}

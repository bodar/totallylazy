package com.googlecode.totallylazy.parser;

import com.googlecode.totallylazy.functions.Function1;
import com.googlecode.totallylazy.Either;
import com.googlecode.totallylazy.functions.Functions;
import com.googlecode.totallylazy.Option;
import com.googlecode.totallylazy.Segment;

import java.util.NoSuchElementException;

import static com.googlecode.totallylazy.Option.some;

public abstract class Success<A> implements Result<A> {
    public static <A> Success<A> success(final A value, final Segment<Character> remainder) {
        return new Success<A>() {
            @Override
            public Segment<Character> remainder() {
                return remainder;
            }

            @Override
            public A value() {
                return value;
            }
        };
    }

    @Override
    public <S> Result<S> map(Function1<? super A, ? extends S> callable) {
        return success(Functions.call(callable, value()), remainder());
    }

    @Override
    public Option<A> option() {
        return some(value());
    }

    @Override
    public Either<String, A> either() {
        return Either.right(value());
    }

    @Override
    public boolean success() {
        return true;
    }

    @Override
    public boolean failure() {
        return false;
    }

    @Override
    public String message() {
        throw new NoSuchElementException();
    }

    @Override
    public String toString() {
        return "Success(" + value() + ")";
    }
}

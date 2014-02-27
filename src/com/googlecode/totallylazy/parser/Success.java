package com.googlecode.totallylazy.parser;

import com.googlecode.totallylazy.Callable1;
import com.googlecode.totallylazy.Either;
import com.googlecode.totallylazy.Function1;
import com.googlecode.totallylazy.Functions;
import com.googlecode.totallylazy.Option;
import com.googlecode.totallylazy.Segment;

import java.util.NoSuchElementException;

import static com.googlecode.totallylazy.Option.some;

public class Success<A> implements Result<A> {
    private final A value;
    private final Segment<Character> remainder;

    private Success(A value, Segment<Character> remainder) {
        this.value = value;
        this.remainder = remainder;
    }

    public static <A> Success<A> success(A value, Segment<Character> remainder) {
        return new Success<A>(value, remainder);
    }

    @Override
    public A value() {
        return value;
    }

    @Override
    public <S> Result<S> map(Callable1<? super A, ? extends S> callable) {
        return success(Functions.call(callable, value), remainder);
    }

    @Override
    public Option<A> option() {
        return some(value);
    }

    @Override
    public Either<String, A> either() {
        return Either.right(value);
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
    public Segment<Character> remainder() {
        return remainder;
    }

    public static class functions {
        public static <A> Function1<Result<A>, Segment<Character>> remainder() {
            return new Function1<Result<A>, Segment<Character>>() {
                @Override
                public Segment<Character> call(Result<A> result) throws Exception {
                    return result.remainder();
                }
            };
        }
    }
}

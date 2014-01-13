package com.googlecode.totallylazy.parser;

import com.googlecode.totallylazy.Function;
import com.googlecode.totallylazy.Pair;

import java.nio.CharBuffer;
import java.util.concurrent.Callable;

import static com.googlecode.totallylazy.Functions.returns;

public class Success<A> extends Pair<A, CharBuffer> implements Result<A> {
    private Success(Callable<? extends A> a, Callable<? extends CharBuffer> remainder) {
        super(a, remainder);
    }

    public static <A> Success<A> success(A value, CharBuffer second) {
        return success(returns(value), returns(second));
    }

    public static <A> Success<A> success(Callable<? extends A> a, Callable<? extends CharBuffer> remainder) {
        return new Success<A>(a, remainder);
    }

    @Override
    public <S> Success<S> map(Function<? super A, ? extends S> callable) {
        return success(callable.apply(value()), second());
    }

    @Override
    public CharBuffer remainder() {
        return second();
    }

    public static class functions {
        public static <A> Function<Result<A>, CharBuffer> remainder() {
            return new Function<Result<A>, CharBuffer>() {
                @Override
                public CharBuffer call(Result<A> result) throws Exception {
                    return ((Success<A>) result).remainder();
                }
            };
        }
    }
}

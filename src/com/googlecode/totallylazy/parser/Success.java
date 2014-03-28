package com.googlecode.totallylazy.parser;

import com.googlecode.totallylazy.Callable1;
import com.googlecode.totallylazy.Function1;
import com.googlecode.totallylazy.Functions;
import com.googlecode.totallylazy.Pair;
import com.googlecode.totallylazy.Segment;

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
    public <S> Success<S> map(Callable1<? super A, ? extends S> callable) {
        return success(Functions.call(callable, value()), second());
    }

    @Override
    public CharBuffer remainder() {
        return second();
    }

    public static class functions {
        public static <A> Function1<Result<A>, CharBuffer> remainder() {
            return new Function1<Result<A>, CharBuffer>() {
                @Override
                public CharBuffer call(Result<A> result) throws Exception {
                    return ((Success<A>) result).remainder();
                }
            };
        }
    }
}

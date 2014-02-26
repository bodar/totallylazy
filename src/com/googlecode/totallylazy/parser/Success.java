package com.googlecode.totallylazy.parser;

import com.googlecode.totallylazy.Callable1;
import com.googlecode.totallylazy.Either;
import com.googlecode.totallylazy.Function1;
import com.googlecode.totallylazy.Functions;
import com.googlecode.totallylazy.Option;
import com.googlecode.totallylazy.Pair;
import com.googlecode.totallylazy.Segment;

import java.util.concurrent.Callable;

import static com.googlecode.totallylazy.Functions.returns;
import static com.googlecode.totallylazy.Option.some;

public class Success<A> extends Pair<A, Segment<Character>> implements Result<A>{
    private Success(Callable<? extends A> a, Callable<? extends Segment<Character>> remainder) {
        super(a, remainder);
    }

    public static <A> Success<A> success(A value, Segment<Character> second) {
        return success(returns(value), returns(second));
    }

    public static <A> Success<A> success(Callable<? extends A> a, Callable<? extends Segment<Character>> remainder) {
        return new Success<A>(a, remainder);
    }

    @Override
    public <S> Success<S> map(Callable1<? super A, ? extends S> callable) {
        return success(Functions.call(callable, value()), second());
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
    public Segment<Character> remainder() {
        return second();
    }

    public static class functions {
        public static <A> Function1<Result<A>, Segment<Character>> remainder() {
            return new Function1<Result<A>, Segment<Character>>() {
                @Override
                public Segment<Character> call(Result<A> result) throws Exception {
                    return ((Success<A>)result).remainder();
                }
            };
        }
    }
}

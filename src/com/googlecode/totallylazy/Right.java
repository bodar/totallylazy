package com.googlecode.totallylazy;

import static com.googlecode.totallylazy.Callers.call;

public final class Right<L,R> extends Either<L,R> {
    private final R value;

    private Right(R value) {
        this.value = value;
    }

    public static <L,R> Right<L,R> right(R value) {
        return new Right<L,R>(value);
    }

    @Override
    public boolean isRight() {
        return true;
    }

    @Override
    public R right() {
        return value;
    }

    @Override
    public <S> S fold(S seed, Callable2<? super S, ? super L, ? extends S> left, Callable2<? super S, ? super R, ? extends S> right) {
        return call(right, seed, right());
    }

    @Override
    public <S> S map(Callable1<? super L, S> left, Callable1<? super R, ? extends S> right) {
        return call(right, right());
    }

    @Override
    public <S> Either<L, S> map(Callable1<? super R, ? extends S> callable) {
        return right(call(callable, right()));
    }

    @Override
    public <S> Either<L, S> flatMap(Callable1<? super R, ? extends Either<L, S>> callable) {
        return call(callable, right());
    }

    @Override
    public String toString() {
        return "right(" + value + ")";
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof Right && ((Right) o).value.equals(value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @Override
    public Object value() {
        return value;
    }
}

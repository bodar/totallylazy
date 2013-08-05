package com.googlecode.totallylazy;

import com.googlecode.totallylazy.iterators.EmptyIterator;

import java.util.Iterator;

import static com.googlecode.totallylazy.Callers.call;
import static com.googlecode.totallylazy.Objects.equalTo;

public final class Left<L,R> extends Either<L, R> {
    private final L value;

    private Left(L value) {
        this.value = value;
    }

    public static <L,R> Left<L,R> left(L value) {
        return new Left<L,R>(value);
    }

    @Override
    public boolean isLeft() {
        return true;
    }

    @Override
    public L left() {
        return value;
    }

    @Override
    public <S> S fold(S seed, Callable2<? super S, ? super L, ? extends S> left, Callable2<? super S, ? super R, ? extends S> right) {
        return call(left, seed, left());
    }

    @Override
    public <S> S map(Callable1<? super L, S> left, Callable1<? super R, ? extends S> right) {
        return call(left, left());
    }

    @Override
    public <S> Either<L, S> map(Callable1<? super R, ? extends S> callable) {
        return left(left());
    }

    @Override
    public <S> Either<L, S> flatMap(Callable1<? super R, ? extends Either<L, S>> callable) {
        return left(left());
    }

    @Override
    public String toString() {
        return "left(" + value + ")";
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof Left && equalTo(((Left) o).value, value);
    }

    @Override
    public int hashCode() {
        return value == null ? 19 : value.hashCode();
    }

    @Override
    public Object value() {
        return value;
    }

    @Override
    public <S> S fold(S seed, Callable2<? super S, ? super R, ? extends S> callable) {
        return seed;
    }

    @Override
    public Iterator<R> iterator() {
        return new EmptyIterator<R>();
    }
}
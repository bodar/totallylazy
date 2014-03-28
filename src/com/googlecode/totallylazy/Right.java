package com.googlecode.totallylazy;

import java.util.Iterator;
import java.util.NoSuchElementException;

import static com.googlecode.totallylazy.Callers.call;
import static com.googlecode.totallylazy.Objects.equalTo;
import static com.googlecode.totallylazy.Option.none;
import static com.googlecode.totallylazy.Option.some;
import static com.googlecode.totallylazy.Sequences.one;

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
    public boolean isLeft() {
        return false;
    }

    @Override
    public R right() {
        return value;
    }

    @Override
    public L left() {
        throw new NoSuchElementException();
    }

    @Override
    public Either<R, L> flip() {
        return left(value);
    }

    @Override
    public Option<L> leftOption() {
        return none();
    }

    @Override
    public Option<R> rightOption() {
        return some(value);
    }

    @Override
    public <S> S fold(S seed, Callable2<? super S, ? super L, ? extends S> left, Callable2<? super S, ? super R, ? extends S> right) {
        return call(right, seed, right());
    }

    @Override
    public <S> S map(Function1<? super L, S> left, Function1<? super R, ? extends S> right) {
        return call(right, right());
    }

    @Override
    public <S> Either<L, S> map(Function1<? super R, ? extends S> callable) {
        return right(call(callable, right()));
    }

    @Override
    public <S> Either<L, S> flatMap(Function1<? super R, ? extends Either<L, S>> callable) {
        return call(callable, right());
    }

    @Override
    public String toString() {
        return "right(" + value + ")";
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof Right && equalTo(((Right) o).value, value);
    }

    @Override
    public int hashCode() {
        return value == null ? 31 : value.hashCode();
    }

    @Override
    public Object value() {
        return value;
    }

    @Override
    public <S> S fold(S seed, Callable2<? super S, ? super R, ? extends S> callable) {
        return call(callable, seed, value);
    }

    @Override
    public Iterator<R> iterator() {
        return one(value).iterator();
    }
}

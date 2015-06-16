package com.googlecode.totallylazy;

import com.googlecode.totallylazy.iterators.EmptyIterator;

import java.util.Iterator;
import java.util.NoSuchElementException;

import static com.googlecode.totallylazy.Callers.call;
import static com.googlecode.totallylazy.Objects.equalTo;
import static com.googlecode.totallylazy.Option.none;
import static com.googlecode.totallylazy.Option.some;

public final class Left<L,R> extends Either<L, R> {
    private final L value;

    private Left(L value) {
        this.value = value;
    }

    public static <L,R> Left<L,R> left(L value) {
        return new Left<L,R>(value);
    }

    @Override
    public boolean isRight() {
        return false;
    }

    @Override
    public boolean isLeft() {
        return true;
    }

    @Override
    public R right() {
        throw new NoSuchElementException();
    }

    @Override
    public L left() {
        return value;
    }

    @Override
    public Either<R, L> flip() {
        return right(value);
    }

    @Override
    public Option<L> leftOption() {
        return some(value);
    }

    @Override
    public Option<R> rightOption() {
        return none();
    }

    @Override
    public <S> S fold(S seed, Function2<? super S, ? super L, ? extends S> left, Function2<? super S, ? super R, ? extends S> right) {
        return call(left, seed, left());
    }

    @Override
    public <S> S map(Function1<? super L, S> left, Function1<? super R, ? extends S> right) {
        return call(left, left());
    }

    @Override
    public <S> Either<L, S> map(Function1<? super R, ? extends S> callable) {
        return left(left());
    }

    @Override
    public <S> Either<S, R> mapLeft(Function1<? super L, ? extends S> callable) {
        return left(call(callable, left()));
    }

    @Override
    public <S> Either<L, S> flatMap(Function1<? super R, ? extends Either<L, S>> callable) {
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
    public <S> S fold(S seed, Function2<? super S, ? super R, ? extends S> callable) {
        return seed;
    }

    @Override
    public Iterator<R> iterator() {
        return new EmptyIterator<R>();
    }
}
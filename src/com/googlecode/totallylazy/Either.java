package com.googlecode.totallylazy;

import java.util.NoSuchElementException;

import static com.googlecode.totallylazy.Callers.call;

public abstract class Either<L, R> {
    public boolean isRight() {
        return false;
    }

    public boolean isLeft() {
        return false;
    }

    public R right(){
        throw new NoSuchElementException();
    }

    public L left(){
        throw new NoSuchElementException();
    }

    public <S> S fold(final S seed, final Callable2<? super S, L, S> left, final Callable2<? super S, R, S> right) {
        return isLeft() ? call(left, seed, left()) : call(right, seed, right());
    }

    public <S> S map(final Callable1<? super L, S> left, final Callable1<? super R, S> right) {
        return isLeft() ? call(left, left()) : call(right, right());
    }

    public abstract Object value();
}

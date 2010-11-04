package com.googlecode.totallylazy;

import java.util.NoSuchElementException;

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
        return isLeft() ? Callers.call(left, seed, left()) : Callers.call(right, seed, right());
    }
}

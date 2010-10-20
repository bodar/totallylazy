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
}

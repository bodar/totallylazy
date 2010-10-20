package com.googlecode.totallylazy;

public class Right<L,R> extends Either<L,R> {
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
}

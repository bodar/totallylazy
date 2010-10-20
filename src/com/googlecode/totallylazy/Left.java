package com.googlecode.totallylazy;

public class Left<L,R> extends Either<L, R> {
    private final L value;

    public Left(L value) {
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
}
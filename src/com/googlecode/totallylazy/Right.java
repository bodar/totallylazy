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
}

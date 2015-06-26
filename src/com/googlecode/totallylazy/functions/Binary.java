package com.googlecode.totallylazy.functions;

public interface Binary<T> extends Function2<T, T, T> {
    static <T> Binary<T> binary(final Function2<T, T, T> callable) {
        return callable::call;
    }
}
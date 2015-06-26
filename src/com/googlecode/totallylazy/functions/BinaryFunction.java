package com.googlecode.totallylazy.functions;

public interface BinaryFunction<T> extends Function2<T, T, T> {
    static <T> BinaryFunction<T> binary(final Function2<T, T, T> callable) {
        return callable::call;
    }
}
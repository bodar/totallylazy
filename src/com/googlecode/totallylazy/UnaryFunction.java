package com.googlecode.totallylazy;

public interface UnaryFunction<T> extends Function1<T, T> {
    static <T> UnaryFunction<T> unary(final Function1<T, T> callable) {
        return callable::call;
    }
}

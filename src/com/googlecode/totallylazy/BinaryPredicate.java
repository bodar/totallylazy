package com.googlecode.totallylazy;

import java.util.function.BiPredicate;

public interface BinaryPredicate<T> extends BiPredicate<T,T> {
    boolean matches(T a, T b);

    @Override
    default boolean test(T t, T t2) {
        return matches(t, t2);
    }
}

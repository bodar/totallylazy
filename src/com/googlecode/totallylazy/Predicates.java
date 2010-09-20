package com.googlecode.totallylazy;

public class Predicates {
    public static <T> Predicate<T> is(final T t) {
        return new Predicate<T>() {
            public boolean matches(T other) {
                return other.equals(t);
            }
        };
    }

    public static Predicate<Integer> even() {
        return new Predicate<Integer>() {
            public boolean matches(Integer other) {
                return other % 2 == 0;
            }
        };
    }
}

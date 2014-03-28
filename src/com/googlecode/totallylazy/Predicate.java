package com.googlecode.totallylazy;

public interface Predicate<T> {
    boolean matches(T other);
}

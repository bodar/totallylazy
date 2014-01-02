package com.googlecode.totallylazy;

public interface Filterable<T> {
    Filterable<T> filter(final Predicate<? super T> predicate);
}

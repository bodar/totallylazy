package com.googlecode.totallylazy.predicates;

import com.googlecode.totallylazy.Predicate;

public interface Between<T> extends Predicate<T> {
    T lower();

    T upper();
}

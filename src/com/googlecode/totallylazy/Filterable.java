package com.googlecode.totallylazy;

import com.googlecode.totallylazy.predicates.Predicate;

import static com.googlecode.totallylazy.predicates.Predicates.not;

public interface Filterable<T> {
    Filterable<T> filter(final Predicate<? super T> predicate);

    default Filterable<T> reject(final Predicate<? super T> predicate) {
        return filter(not(predicate));
    }

}

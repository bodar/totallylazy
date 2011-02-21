package com.googlecode.totallylazy;

import com.googlecode.totallylazy.predicates.LogicalPredicate;

import static com.googlecode.totallylazy.Sequences.sequence;

public class Arrays {
    public static <T> LogicalPredicate<? super T[]> exists(final Predicate<? super T> predicate) {
        return new LogicalPredicate<T[]>() {
            public boolean matches(T[] array) {
                return sequence(array).exists(predicate);
            }
        };
    }
}

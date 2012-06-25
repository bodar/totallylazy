package com.googlecode.totallylazy;

import com.googlecode.totallylazy.predicates.LogicalPredicate;

import java.util.List;

import static com.googlecode.totallylazy.Sequences.sequence;

public class Arrays {
    public static <T> LogicalPredicate<T[]> exists(final Predicate<? super T> predicate) {
        return new LogicalPredicate<T[]>() {
            public boolean matches(T[] array) {
                return sequence(array).exists(predicate);
            }
        };
    }

    public static <T> LogicalPredicate<T[]> empty() {
        return new LogicalPredicate<T[]>() {
            public boolean matches(T[] array) {
                return array.length == 0;
            }
        };
    }

    public static <T> List<T> list(T... values) {
        return Lists.list(values);
    }

    public static <T> boolean containsIndex(T[] array, int index) {
        return index < array.length;
    }
}

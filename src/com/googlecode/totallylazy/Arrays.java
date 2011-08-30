package com.googlecode.totallylazy;

import com.googlecode.totallylazy.predicates.LogicalPredicate;
import com.googlecode.totallylazy.records.Keyword;

import java.util.List;

import static com.googlecode.totallylazy.Sequences.sequence;

public class Arrays {
    public static <T> LogicalPredicate<? super T[]> exists(final Predicate<? super T> predicate) {
        return new LogicalPredicate<T[]>() {
            public boolean matches(T[] array) {
                return sequence(array).exists(predicate);
            }
        };
    }

    public static <T> Predicate<T[]> empty() {
        return new Predicate<T[]>() {
            public boolean matches(T[] array) {
                return array.length == 0;
            }
        };
    }

    public static <T> List<T> list(T... values) {
        return java.util.Arrays.asList(values);
    }
}

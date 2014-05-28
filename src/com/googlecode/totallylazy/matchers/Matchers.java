package com.googlecode.totallylazy.matchers;

import com.googlecode.totallylazy.Function;
import com.googlecode.totallylazy.Predicate;
import com.googlecode.totallylazy.Predicates;
import com.googlecode.totallylazy.predicates.LogicalPredicate;

import static com.googlecode.totallylazy.Sequences.sequence;

public class Matchers {

    public static <T> Iterable<Predicate<T>> are(final Iterable<T> values, Class<T> clazz) {
        return are(values);
    }

    public static <T> Iterable<Predicate<T>> are(final Iterable<T> values) {
        return sequence(values).map(Matchers.<T>isPredicate());
    }

    public static <T> Function<T, Predicate<T>> isPredicate(Class<T> clazz) {
        return isPredicate();
    }

    public static <T> Function<T, Predicate<T>> isPredicate() {
        return new Function<T, Predicate<T>>() {
            @Override
            public Predicate<T> call(T t) throws Exception {
                return Predicates.is(t);
            }
        };
    }

    public static <T> LogicalPredicate<T> predicate(final Predicate<T> Predicate) {
        return new LogicalPredicate<T>() {
            public final boolean matches(T other) {
                return Predicate.matches(other);
            }
        };
    }

}

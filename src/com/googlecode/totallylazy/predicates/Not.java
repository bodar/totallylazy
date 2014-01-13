package com.googlecode.totallylazy.predicates;

import com.googlecode.totallylazy.Mapper;
import com.googlecode.totallylazy.Predicate;
import com.googlecode.totallylazy.Unchecked;
import com.googlecode.totallylazy.annotations.multimethod;

public class Not<T> extends AbstractPredicate<T> {
    private final Predicate<T> predicate;

    private Not(Predicate<T> predicate) {
        this.predicate = predicate;
    }

    public static <T> Not<T> not(Predicate<? super T> predicate) {
        return new Not<T>(Unchecked.<Predicate<T>>cast(predicate));
    }

    public boolean matches(T other) {
        return !predicate.matches(other);
    }

    public Predicate<T> predicate() {
        return predicate;
    }

    @Override
    public int hashCode() {
        return 31 * predicate.hashCode();
    }

    @multimethod
    public boolean equals(Not other) {
        return predicate.equals(other.predicate());
    }

    @Override
    public String toString() {
        return "not " + predicate().toString();
    }

    public static class functions {
        public static <T> Mapper<Not<T>, Predicate<T>> predicate() {
            return new Mapper<Not<T>, Predicate<T>>() {
                @Override
                public Predicate<T> call(Not<T> not) throws Exception {
                    return not.predicate();
                }
            };
        }
    }
}

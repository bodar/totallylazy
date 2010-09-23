package com.googlecode.totallylazy;

import com.googlecode.totallylazy.predicates.*;

public class Predicates {
    public static <T> Predicate<T> is(final T t) {
        return new Is<T>(t);
    }

    public static <T> Predicate<T> not(final T t) {
        return new Not<T>(is(t));
    }
    public static <T> Predicate<T> not(final Predicate<T> t) {
        return new Not<T>(t);
    }

    public static <T> Predicate<T> countTo(final int count) {
        return new CountTo<T>(count);
    }

    public static <T> Predicate<T> whileTrue(final Predicate<T> t) {
        return new WhileTrue<T>(t);
    }

    public static Predicate<Integer> even() {
        return new ModRemainderIs(2, 0);
    }

    public static Predicate<Integer> odd() {
        return new ModRemainderIs(2, 1);
    }

    public static <T> Predicate<T> notNull(Class<T> aClass) {
        return new NotNullPredicate<T>();
    }
}

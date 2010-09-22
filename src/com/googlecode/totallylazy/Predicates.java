package com.googlecode.totallylazy;

import com.googlecode.totallylazy.predicates.Is;
import com.googlecode.totallylazy.predicates.ModRemainderIs;
import com.googlecode.totallylazy.predicates.Not;

public class Predicates {
    public static <T> Predicate<T> is(final T t) {
        return new Is<T>(t);
    }

    public static <T> Predicate<T> not(final T t) {
        return new Not<T>(is(t));
    }

    public static Predicate<Integer> even() {
        return new ModRemainderIs(2, 0);
    }
    public static Predicate<Integer> odd() {
        return new ModRemainderIs(2, 1);
    }

}

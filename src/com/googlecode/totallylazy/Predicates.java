package com.googlecode.totallylazy;

import com.googlecode.totallylazy.predicates.Is;
import com.googlecode.totallylazy.predicates.ModRemainderIs;

public class Predicates {
    public static <T> Predicate<T> is(final T t) {
        return new Is<T>(t);
    }

    public static Predicate<Integer> even() {
        return new ModRemainderIs(2, 0);
    }
    public static Predicate<Integer> odd() {
        return new ModRemainderIs(2, 1);
    }

}

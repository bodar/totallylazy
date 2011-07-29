package com.googlecode.totallylazy.callables;

import com.googlecode.totallylazy.Callable2;

import static com.googlecode.totallylazy.numbers.Numbers.increment;

public class CountNotNull<T extends Number> implements Callable2<T, Object, Number> {
    public Number call(T a, Object b) throws Exception {
        return b != null ? increment(a) : a;
    }

    public static <T extends Number> Callable2<T, Object, Number> count() {
        return new CountNotNull<T>();
    }
}

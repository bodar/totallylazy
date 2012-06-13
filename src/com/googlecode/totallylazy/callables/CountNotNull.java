package com.googlecode.totallylazy.callables;

import com.googlecode.totallylazy.Callable2;

import static com.googlecode.totallylazy.numbers.Numbers.increment;

public class CountNotNull implements Callable2<Number, Object, Number> {
    public Number call(Number a, Object b) throws Exception {
        return b != null ? increment(a) : a;
    }

    public static Callable2<Number, Object, Number> count() {
        return new CountNotNull();
    }
}

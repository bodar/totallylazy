package com.googlecode.totallylazy.callables;

import com.googlecode.totallylazy.Callable2;
import com.googlecode.totallylazy.Function2;
import com.googlecode.totallylazy.Identity;

import static com.googlecode.totallylazy.numbers.Numbers.increment;

public class CountNotNull extends Function2<Number, Object, Number> implements Identity<Number> {
    public Number call(Number a, Object b) throws Exception {
        return b != null ? increment(a) : a;
    }

    public static Callable2<Number, Object, Number> count() {
        return new CountNotNull();
    }

    @Override
    public Number identity() {
        return 0;
    }
}

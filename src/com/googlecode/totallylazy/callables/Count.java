package com.googlecode.totallylazy.callables;

import com.googlecode.totallylazy.ReducerFunction;

import static com.googlecode.totallylazy.numbers.Numbers.increment;

public class Count extends ReducerFunction<Object, Number> {
    public Number call(Number a, Object b) throws Exception {
        return b != null ? increment(a) : a;
    }

    public static Count count() {
        return new Count();
    }

    @Override
    public Number identity() {
        return 0;
    }
}

package com.googlecode.totallylazy.functions;

import com.googlecode.totallylazy.numbers.Numbers;

import static com.googlecode.totallylazy.numbers.Numbers.increment;

public enum Count implements CurriedCombiner<Object, Number> {
    instance;

    public Number call(Number a, Object b) throws Exception {
        return b != null ? increment(a) : a;
    }

    public static Count count() {
        return instance;
    }

    @Override
    public Number identity() {
        return 0;
    }

    @Override
    public Number combine(Number a, Number b) throws Exception {
        return Numbers.add(a, b);
    }
}

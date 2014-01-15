package com.googlecode.totallylazy.callables;

import com.googlecode.totallylazy.ReducerCombinerFunction;
import com.googlecode.totallylazy.ReducerFunction;
import com.googlecode.totallylazy.numbers.Numbers;

import static com.googlecode.totallylazy.numbers.Numbers.increment;

public class Count implements ReducerCombinerFunction<Object, Number> {
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

    @Override
    public Number combine(Number a, Number b) throws Exception {
        return Numbers.add(a, b);
    }
}

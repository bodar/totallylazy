package com.googlecode.totallylazy.numbers;

import com.googlecode.totallylazy.Combiner;

public class Sum implements Combiner<Number> {
    public Number call(Number a, Number b) {
        return Numbers.add(a, b);
    }

    @Override
    public Number identityElement() {
        return 0;
    }
}

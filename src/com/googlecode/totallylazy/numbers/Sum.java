package com.googlecode.totallylazy.numbers;

import com.googlecode.totallylazy.Associative;
import com.googlecode.totallylazy.Function2;
import com.googlecode.totallylazy.Identity;

public class Sum extends Function2<Number, Number, Number> implements Identity<Number>, Associative<Number> {
    public Number call(Number a, Number b) {
        return Numbers.add(a, b);
    }

    @Override
    public Number identity() {
        return 0;
    }
}

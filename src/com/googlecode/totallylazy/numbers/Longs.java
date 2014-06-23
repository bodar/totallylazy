package com.googlecode.totallylazy.numbers;

import com.googlecode.totallylazy.comparators.Maximum;
import com.googlecode.totallylazy.comparators.Minimum;

public class Longs {
    public static Maximum.Function<Long> maximum() {
        return Maximum.constructors.maximum(Long.MIN_VALUE);
    }

    public static Minimum.Function<Long> minimum() {
        return Minimum.constructors.minimum(Long.MAX_VALUE);
    }

}

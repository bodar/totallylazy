package com.googlecode.totallylazy.numbers;

import com.googlecode.totallylazy.comparators.Maximum;
import com.googlecode.totallylazy.comparators.Minimum;

public class Longs {
    public static Maximum<Long> maximum() {
        return Maximum.maximum(Long.MIN_VALUE);
    }

    public static Minimum<Long> minimum() {
        return Minimum.minimum(Long.MAX_VALUE);
    }
}

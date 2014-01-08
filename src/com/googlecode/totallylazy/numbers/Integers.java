package com.googlecode.totallylazy.numbers;

import com.googlecode.totallylazy.comparators.Maximum;
import com.googlecode.totallylazy.comparators.Minimum;

public class Integers {
    public static Maximum.Function<Integer> maximum() {
        return Maximum.constructors.maximum(Integer.MIN_VALUE);
    }

    public static Minimum.Function<Integer> minimum() {
        return Minimum.constructors.minimum(Integer.MAX_VALUE);
    }
}

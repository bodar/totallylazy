package com.googlecode.totallylazy.numbers;

import com.googlecode.totallylazy.comparators.Maximum;
import com.googlecode.totallylazy.comparators.Minimum;

public class Integers {
    public static Maximum<Integer> maximum() {
        return Maximum.maximum(Integer.MIN_VALUE);
    }

    public static Minimum<Integer> minimum() {
        return Minimum.minimum(Integer.MAX_VALUE);
    }
}

package com.googlecode.totallylazy.numbers;

import com.googlecode.totallylazy.Seq;
import com.googlecode.totallylazy.comparators.Maximum;
import com.googlecode.totallylazy.comparators.Minimum;

import static com.googlecode.totallylazy.Sequences.iterate;

public class Integers {
    public static Maximum<Integer> maximum() {
        return Maximum.maximum(Integer.MIN_VALUE);
    }

    public static Minimum<Integer> minimum() {
        return Minimum.minimum(Integer.MAX_VALUE);
    }

    public static Seq<Integer> range(final int start) {
        return iterate(i -> i + 1, start);
    }

    public static Seq<Integer> range(final int start, final int end) {
        if (end < start) return range(start, end, -1);
        return range(start).takeWhile(i -> i <= end);
    }

    public static Seq<Integer> range(final int start, final int end, final int step) {
        if (end < start) return iterate(i -> i + step, start).takeWhile(i -> i >= end);
        return iterate(i -> i + step, start).takeWhile(i -> i <= end);
    }
}

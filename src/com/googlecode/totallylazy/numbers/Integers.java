package com.googlecode.totallylazy.numbers;

import com.googlecode.totallylazy.Function1;
import com.googlecode.totallylazy.Sequence;
import com.googlecode.totallylazy.comparators.Maximum;
import com.googlecode.totallylazy.comparators.Minimum;
import com.googlecode.totallylazy.predicates.LogicalPredicate;

import static com.googlecode.totallylazy.Sequences.iterate;
import static com.googlecode.totallylazy.numbers.Integers.functions.greaterThanOrEqualTo;
import static com.googlecode.totallylazy.numbers.Integers.functions.increment;
import static com.googlecode.totallylazy.numbers.Integers.functions.lessThanOrEqualTo;

public class Integers {
    public static Maximum.Function<Integer> maximum() {
        return Maximum.constructors.maximum(Integer.MIN_VALUE);
    }

    public static Minimum.Function<Integer> minimum() {
        return Minimum.constructors.minimum(Integer.MAX_VALUE);
    }

    public static Sequence<Integer> range(final int start) { return iterate(increment, start); }

    public static Sequence<Integer> range(final int start, final int end) {
        if (end < start) return range(start, end, -1);
        return range(start).takeWhile(lessThanOrEqualTo(end));
    }

    public static Sequence<Integer> range(final int start, final int end, final int step) {
        if (end < start) return iterate(increment, start).takeWhile(greaterThanOrEqualTo(end));
        return iterate(increment, start).takeWhile(lessThanOrEqualTo(end));
    }

    public static class functions {
        public static final Function1<Integer, Integer> increment = new Function1<Integer, Integer>() {
            @Override
            public Integer call(Integer i) throws Exception {
                return i + 1;
            }
        };

        public static LogicalPredicate<Integer> lessThanOrEqualTo(final int end) {
            return new LogicalPredicate<Integer>() {
                @Override
                public boolean matches(Integer other) {
                    return other <= end;
                }
            };
        }

        public static LogicalPredicate<Integer> greaterThanOrEqualTo(final int end) {
            return new LogicalPredicate<Integer>() {
                @Override
                public boolean matches(Integer other) {
                    return other >= end;
                }
            };
        }
    }
}

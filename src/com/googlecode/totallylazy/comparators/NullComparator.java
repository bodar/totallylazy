package com.googlecode.totallylazy.comparators;

public class NullComparator {
    public static <T extends Comparable<T>>int compare(T a, T b, Direction direction) {
        if (a == null && b == null) {
            return 0;
        }
        if (a == null) {
            return direction.value();
        }
        if (b == null) {
            return -1 * direction.value();
        }
        return a.compareTo(b);
    }

    public enum Direction {
        Up {
            @Override
            public int value() {
                return 1;
            }
        },
        Down {
            @Override
            public int value() {
                return -1;
            }
        };

        public abstract int value();
    }
}

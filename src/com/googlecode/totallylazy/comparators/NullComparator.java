package com.googlecode.totallylazy.comparators;

import java.util.Comparator;

public class NullComparator {
    public static <T> int compare(T a, T b, Direction direction, Comparator<? super T> comparator) {
        if (a == null && b == null) {
            return 0;
        }
        if (a == null) {
            return direction.value();
        }
        if (b == null) {
            return -1 * direction.value();
        }
        return comparator.compare(a, b);
    }

    public static <T extends Comparable<? super T>> int compare(T a, T b, Direction direction) {
        return compare(a, b, direction, Comparators.<T>ascending());
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

package com.googlecode.totallylazy.comparators;

import com.googlecode.totallylazy.Monoid;

public interface Minimum<T> extends Monoid<T> {
    static <T extends Comparable<? super T>> T minimum(T a, T b) {
        return NullComparator.compare(a, b, NullComparator.Direction.Up) > 0 ? b : a;
    }

    static <T extends Comparable<? super T>> Function<T> minimum(final T identity) {
        return new Function<T>(identity);
    }

    class Function<T extends Comparable<? super T>> implements Minimum<T>, Monoid<T> {
        private final T identity;

        private Function(T identity) {this.identity = identity;}

        @Override
        public T call(T t, T t2) throws Exception {
            return minimum(t, t2);
        }

        @Override
        public T identity() {
            return identity;
        }
    }
}
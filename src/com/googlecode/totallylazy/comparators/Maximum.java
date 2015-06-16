package com.googlecode.totallylazy.comparators;

import com.googlecode.totallylazy.Monoid;

public interface Maximum<T> extends Monoid<T> {
    static <T extends Comparable<? super T>> T maximum(T a, T b) {
        return NullComparator.compare(a, b, NullComparator.Direction.Down) > 0 ? a : b;
    }

    static <T extends Comparable<? super T>> Function<T> maximum(final T identity) {
        return new Function<T>(identity);
    }

    class Function<T extends Comparable<? super T>> implements Maximum<T>, Monoid<T> {
        private final T identity;

        private Function(T identity) {this.identity = identity;}

        @Override
        public T call(T t, T t2) throws Exception {
            return maximum(t, t2);
        }

        @Override
        public T identity() {
            return identity;
        }
    }

}
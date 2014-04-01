package com.googlecode.totallylazy.comparators;

import com.googlecode.totallylazy.Combiner;

public interface Minimum<T> extends Combiner<T> {
    static <T extends Comparable<? super T>> T minimum(T a, T b) {
        return NullComparator.compare(a, b, NullComparator.Direction.Up) > 0 ? b : a;
    }

    static <T extends Comparable<? super T>> Function<T> minimum(final T identity) {
        return new Function<>(identity);
    }

    class Function<T extends Comparable<? super T>> implements Minimum<T> {
        private final T identity;

        private Function(T identity) {this.identity = identity;}

        @Override
        public T call(T t, T t2) throws Exception {
            return Minimum.minimum(t, t2);
        }

        @Override
        public T identityElement() {
            return identity;
        }
    }
}
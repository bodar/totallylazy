package com.googlecode.totallylazy.comparators;

import com.googlecode.totallylazy.Combiner;

public interface Minimum<T> extends Combiner<T> {
    class methods {
        public static <T extends Comparable<? super T>> T minimum(T a, T b) {
            return a.compareTo(b) > 0 ? b : a;
        }
    }

    class constructors {
        public static <T extends Comparable<? super T>> Function<T> minimum(final T identity) {
            return new Function<T>(identity);
        }
    }

    class Function<T extends Comparable<? super T>> implements Minimum<T> {
        private final T identity;

        private Function(T identity) {this.identity = identity;}

        @Override
        public T call(T t, T t2) throws Exception {
            return methods.minimum(t, t2);
        }

        @Override
        public T identity() {
            return identity;
        }
    }
}
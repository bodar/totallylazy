package com.googlecode.totallylazy.comparators;

import com.googlecode.totallylazy.functions.Monoid;
import com.googlecode.totallylazy.functions.CurriedMonoid;

public interface Maximum<T> extends Monoid<T> {
    class methods {
        public static <T extends Comparable<? super T>> T maximum(T a, T b) {
            return NullComparator.compare(a, b, NullComparator.Direction.Down) > 0 ? a : b;
        }
    }

    class constructors {
        public static <T extends Comparable<? super T>> Function<T> maximum(final T identity) {
            return new Function<T>(identity);
        }
    }

    class Function<T extends Comparable<? super T>> implements Maximum<T>,CurriedMonoid<T> {
        private final T identity;

        private Function(T identity) {this.identity = identity;}

        @Override
        public T call(T t, T t2) throws Exception {
            return methods.maximum(t, t2);
        }

        @Override
        public T identity() {
            return identity;
        }
    }

}
package com.googlecode.totallylazy.comparators;

import com.googlecode.totallylazy.Combiner;
import com.googlecode.totallylazy.CombinerFunction;

public interface Maximum<T> extends Combiner<T> {
    class methods {
        public static <T extends Comparable<? super T>> T maximum(T a, T b) {
            return a.compareTo(b) > 0 ? a : b;
        }
    }

    class constructors {
        public static <T extends Comparable<? super T>> Function<T> maximum(final T identity) {
            return new Function<T>(identity);
        }
    }

    class Function<T extends Comparable<? super T>> extends CombinerFunction<T> implements Maximum<T> {
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
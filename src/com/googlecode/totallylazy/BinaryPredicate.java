package com.googlecode.totallylazy;

import com.googlecode.totallylazy.predicates.LogicalBinaryPredicate;

public interface BinaryPredicate<T> {
    boolean matches(T a, T b);

    class constructors {
        public static <T> LogicalBinaryPredicate<T> binary(final Callable2<T,T,Boolean> callable) {
            return new LogicalBinaryPredicate<T>() {
                @Override
                public boolean matches(T t, T t2) {
                    try {
                        return callable.call(t, t2);
                    } catch (Exception e) {
                        return false;
                    }
                }
            };
        }
    }

}

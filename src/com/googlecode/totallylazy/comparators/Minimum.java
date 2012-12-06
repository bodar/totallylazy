package com.googlecode.totallylazy.comparators;

import com.googlecode.totallylazy.Associative;
import com.googlecode.totallylazy.CombinerFunction;
import com.googlecode.totallylazy.Function2;

public class Minimum<T extends Comparable<? super T>> extends Function2<T, T, T> implements Associative<T> {
    public T call(T a, T b) throws Exception {
        return minimum(a, b);
    }

    public static <T extends Comparable<? super T>> T minimum(T a, T b) {
        return a.compareTo(b) > 0 ? b : a;
    }

    public static <T extends Comparable<? super T>> Function2<T, T, T> minimum(Class<T> aClass) {
        return new Minimum<T>();
    }

    public static <T extends Comparable<? super T>> Function2<T, T, T> minimum() {
        return new Minimum<T>();
    }

    public static <T extends Comparable<? super T>> CombinerFunction<T> minimum(final T identity) {
        return new CombinerFunction<T>() {
            @Override
            public T call(T t, T t2) throws Exception {
                return minimum(t, t2);
            }

            @Override
            public T identity() {
                return identity;
            }
        };
    }

}
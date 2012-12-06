package com.googlecode.totallylazy.comparators;

import com.googlecode.totallylazy.Associative;
import com.googlecode.totallylazy.CombinerFunction;
import com.googlecode.totallylazy.Function2;
import com.googlecode.totallylazy.time.Dates;

import java.util.Date;

public class Maximum<T extends Comparable<? super T>> extends Function2<T, T, T> implements Associative<T> {
    public T call(T a, T b) throws Exception {
        return maximum(a, b);
    }

    public static <T extends Comparable<? super T>> T maximum(T a, T b) {
        return a.compareTo(b) > 0 ? a : b;
    }

    public static <T extends Comparable<? super T>> Function2<T, T, T> maximum(Class<T> aClass) {
        return new Maximum<T>();
    }

    public static <T extends Comparable<? super T>> Function2<T, T, T> maximum() {
        return new Maximum<T>();
    }

    public static <T extends Comparable<? super T>> CombinerFunction<T> maximum(final T identity) {
        return new CombinerFunction<T>() {
            @Override
            public T call(T t, T t2) throws Exception {
                return maximum(t, t2);
            }

            @Override
            public T identity() {
                return identity;
            }
        };
    }
}
package com.googlecode.totallylazy.comparators;

import com.googlecode.totallylazy.Associative;
import com.googlecode.totallylazy.Function2;

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
}
package com.googlecode.totallylazy.records;

import com.googlecode.totallylazy.Callable2;

public class Minimum<T extends Comparable<T>>  implements Callable2<T, T, T> {
    public T call(T a, T b) throws Exception {
        return minimum(a, b);
    }

    public static <T extends Comparable<T>> T minimum(T a, T b) {
        return a.compareTo(b) > 0 ? b : a;
    }

    public static <T extends Comparable<T>>  Callable2<? super T, ? super T, T> minimum(Class<T> aClass) {
        return new Minimum<T>();
    }
}

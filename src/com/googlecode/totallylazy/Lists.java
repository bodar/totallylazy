package com.googlecode.totallylazy;

import java.util.List;

public class Lists {
    public static <T> List<T> list(T... values) {
        return java.util.Arrays.asList(values);
    }

    public static <T> Function1<T, Integer> indexIn(final List<? extends T> values){
        return new Function1<T, Integer>() {
            @Override
            public Integer call(T t) throws Exception {
                return values.indexOf(t);
            }
        };
    }
}

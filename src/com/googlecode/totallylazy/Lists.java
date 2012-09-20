package com.googlecode.totallylazy;

import java.util.List;

import static com.googlecode.totallylazy.Sequences.sequence;

public class Lists {
    public static <T> List<T> list(T... values) {
        return list(sequence(values));
    }

    public static <T> List<T> list(Iterable<T> iterable) {
        return sequence(iterable).toList();
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

package com.googlecode.totallylazy;

import java.util.ArrayList;
import java.util.List;

import static com.googlecode.totallylazy.Sequences.sequence;

public class Lists {
    @SafeVarargs
    public static <T> List<T> list(T... values) {
        return list(sequence(values));
    }

    public static <T> List<T> list(Iterable<? extends T> iterable) {
        return sequence(iterable).toList();
    }

    public static <T> Function<T, Integer> indexIn(final List<? extends T> values) {
        return new Function<T, Integer>() {
            @Override
            public Integer call(T t) throws Exception {
                return values.indexOf(t);
            }
        };
    }

    public static class functions {
        public static <T> ReducerCombiner<T, List<T>> add() {
            return new ReducerCombiner<T, List<T>>() {
                @Override
                public List<T> combine(List<T> a, List<T> b) throws Exception {
                    List<T> result = identityElement();
                    result.addAll(a);
                    result.addAll(b);
                    return result;
                }

                @Override
                public List<T> call(List<T> rs, T t) throws Exception {
                    rs.add(t);
                    return rs;
                }

                @Override
                public List<T> identityElement() {
                    return new ArrayList<T>();
                }
            };
        }
    }
}

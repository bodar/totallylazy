package com.googlecode.totallylazy;

import java.util.ArrayList;
import java.util.List;

import static com.googlecode.totallylazy.Sequences.sequence;

public class Lists {
    public static <T> List<T> list() {
        return new ArrayList<>();
    }

    @SafeVarargs
    public static <T> List<T> list(T... values) {
        return list(sequence(values));
    }

    public static <T> List<T> list(Iterable<? extends T> iterable) {
        return sequence(iterable).toList();
    }

    public static <T> Function1<T, Integer> indexIn(final List<? extends T> values) {
        return values::indexOf;
    }

    public static class functions {
        public static <T> CurriedCombiner<T, List<T>> add() {
            return new CurriedCombiner<T, List<T>>() {
                @Override
                public List<T> combine(List<T> a, List<T> b) throws Exception {
                    List<T> result = identity();
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
                public List<T> identity() {
                    return new ArrayList<T>();
                }
            };
        }
    }
}

package com.googlecode.totallylazy.functions;

import com.googlecode.totallylazy.Unchecked;

public class FirstCombiner<T> implements CurriedMonoid<T> {
    private static final FirstCombiner<?> instance = new FirstCombiner<>();
    private FirstCombiner() {}

    public static <T> FirstCombiner<T> first() {return Unchecked.cast(instance);}
    public static <T> FirstCombiner<T> first(Class<T> aClass) {return first();}

    @Override
    public T call(T a, T b) throws Exception {
        return a != null ? a : b;
    }

    @Override
    public T identity() {
        return null;
    }
}

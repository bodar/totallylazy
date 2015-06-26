package com.googlecode.totallylazy.functions;

import com.googlecode.totallylazy.Unchecked;

public class LastCombiner<T> implements CurriedMonoid<T> {
    private static final LastCombiner<?> instance = new LastCombiner<>();
    private LastCombiner() {}

    public static <T> LastCombiner<T> last() {return Unchecked.cast(instance); }
    public static <T> LastCombiner<T> last(Class<T> aClass) {return last();}

    @Override
    public T call(T a, T b) throws Exception {
        return b != null ? b : a;
    }

    @Override
    public T identity() {
        return null;
    }
}

package com.googlecode.totallylazy;

public class FirstCombiner<T> implements Combiner<T> {
    private FirstCombiner() {}

    public static <T> FirstCombiner<T> first() {return new FirstCombiner<T>();}
    public static <T> FirstCombiner<T> first(Class<T> aClass) {return first();}

    @Override
    public T call(T a, T b) throws Exception {
        return a != null ? a : b;
    }

    @Override
    public T identityElement() {
        return null;
    }
}

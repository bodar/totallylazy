package com.googlecode.totallylazy;

public class LastCombiner<T> extends CombinerFunction<T> {
    private LastCombiner() {}

    public static <T> LastCombiner<T> last() {return new LastCombiner<T>();}
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

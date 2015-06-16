package com.googlecode.totallylazy.callables;

import com.googlecode.totallylazy.Callables;
import com.googlecode.totallylazy.CombinerFunction;
import com.googlecode.totallylazy.Function;
import com.googlecode.totallylazy.Functions;

public class Compose<T> extends CombinerFunction<Function<T, T>> {
    private Compose() {}

    public static <T> Compose<T> compose() {
        return new Compose<T>();
    }

    public static <T> Compose<T> compose(Class<T> aClass) {
        return compose();
    }

    @Override
    public Function<T, T> call(Function<T, T> a, Function<T, T> b) throws Exception {
        return Callables.compose(a, b);
    }

    @Override
    public Function<T, T> identity() {
        return Functions.identity();
    }

}
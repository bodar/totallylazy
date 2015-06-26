package com.googlecode.totallylazy.functions;

import com.googlecode.totallylazy.Unchecked;

public class Compose<T> implements CurriedMonoid<Function1<T,T>> {
    private static final Compose<?> instance = new Compose<>();
    private Compose() {}

    public static <T> Compose<T> compose() {
        return Unchecked.cast(instance);
    }

    public static <T> Compose<T> compose(Class<T> aClass) {
        return compose();
    }

    @Override
    public Function1<T, T> call(Function1<T, T> a, Function1<T, T> b) throws Exception {
        return Callables.compose(a, b);
    }

    @Override
    public Function1<T, T> identity() {
        return Functions.identity();
    }

}
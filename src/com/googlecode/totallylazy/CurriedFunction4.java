package com.googlecode.totallylazy;

public interface CurriedFunction4<A, B, C, D, E> extends CurriedFunction3<A, B, C, Function1<D, E>>, Function4<A, B, C, D, E> {
    @Override
    default Function1<D, E> call(final A a, final B b, final C c) throws Exception {
        return d -> call(a,b,c,d);
    }

}

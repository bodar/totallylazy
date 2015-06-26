package com.googlecode.totallylazy.functions;

public interface CurriedFunction3<A, B, C, D> extends CurriedFunction2<A, B, Function1<C, D>>, Function3<A, B, C, D> {
    @Override
    default Function1<C, D> call(final A a, final B b) throws Exception {
        return c -> call(a,b,c);
    }
}

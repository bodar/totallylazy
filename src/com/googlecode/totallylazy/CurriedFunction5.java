package com.googlecode.totallylazy;

public interface CurriedFunction5<A, B, C, D, E, F> extends CurriedFunction4<A, B, C, D, Function1<E, F>>, Function5<A, B, C, D, E, F> {
    @Override
    default Function1<E, F> call(final A a, final B b, final C c, final D d) throws Exception {
        return e -> call(a,b,c,d,e);
    }

}

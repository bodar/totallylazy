package com.googlecode.totallylazy;

public abstract class Curried5<A, B, C, D, E, F> extends Curried4<A, B, C, D, Function1<E, F>> implements Function5<A, B, C, D, E, F> {
    @Override
    public Function1<E, F> call(final A a, final B b, final C c, final D d) throws Exception {
        return e -> call(a,b,c,d,e);
    }

    public F apply(final A a, final B b, final C c, final D d, final E e) {
        return Functions.call(this, a, b, c, d, e);
    }

    public Function1<Quintuple<A, B, C, D, E>, F> quintuple() {
        return Functions.quintuple(this);
    }
}

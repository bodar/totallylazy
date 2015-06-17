package com.googlecode.totallylazy;

public abstract class Curried5<A, B, C, D, E, F> extends Curried4<A, B, C, D, Function<E, F>> implements Function5<A, B, C, D, E, F> {
    @Override
    public Function<E, F> call(final A a, final B b, final C c, final D d) throws Exception {
        return Functions.<A, B, C, D, E, F>apply(this, a).apply(b).apply(c).apply(d);
    }

    public F apply(final A a, final B b, final C c, final D d, final E e) {
        return Functions.call(this, a, b, c, d, e);
    }

    public Function<Quintuple<A, B, C, D, E>, F> quintuple() {
        return Functions.quintuple(this);
    }
}

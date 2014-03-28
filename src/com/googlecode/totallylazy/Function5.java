package com.googlecode.totallylazy;

public abstract class Function5<A, B, C, D, E, F> implements Function4<A, B, C, D, Function<E, F>>, Callable5<A, B, C, D, E, F> {
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

package com.googlecode.totallylazy;

public interface Function5<A, B, C, D, E, F> extends Function4<A, B, C, D, Function<E, F>> {
    F call(A a, B b, C c, D d, E e) throws Exception;

    @Override
    default Function<E, F> call(final A a, final B b, final C c, final D d) throws Exception {
        return Functions.<A, B, C, D, E, F>apply(this, a).apply(b).apply(c).apply(d);
    }

    default F apply(final A a, final B b, final C c, final D d, final E e) {
        return Functions.call(this, a, b, c, d, e);
    }

    default Function<Quintuple<A, B, C, D, E>, F> quintuple() {
        return Functions.quintuple(this);
    }
}

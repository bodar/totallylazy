package com.googlecode.totallylazy;

public interface Function5<A, B, C, D, E, F> {
    F call(A a, B b, C c, D d, E e) throws Exception;

    default F apply(final A a, final B b, final C c, final D d, final E e) {
        return Functions.call(this, a, b, c, d, e);
    }

    default Function1<Quintuple<A, B, C, D, E>, F> quintuple() {
        return Functions.quintuple(this);
    }

}

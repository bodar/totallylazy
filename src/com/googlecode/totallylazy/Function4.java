package com.googlecode.totallylazy;

public interface Function4<A, B, C, D, E> {
    E call(A a, B b, C c, D d) throws Exception;

    default E apply(final A a, final B b, final C c, final D d) {
        return Functions.call(this, a, b, c, d);
    }

    default Function1<Quadruple<A, B, C, D>, E> quadruple() {
        return Functions.quadruple(this);
    }

}

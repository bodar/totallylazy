package com.googlecode.totallylazy;

public interface Function4<A, B, C, D, E> extends Function3<A, B, C, Function<D, E>> {
    E call(A a, B b, C c, D d) throws Exception;

    @Override
    default Function<D, E> call(final A a, final B b, final C c) throws Exception {
        return Functions.<A, B, C, D, E>apply(this, a).apply(b).apply(c);
    }

    default E apply(final A a, final B b, final C c, final D d){
        return Functions.call(this, a, b, c, d);
    }

    default Function<Quadruple<A, B, C, D>, E> quadruple() {
        return Functions.quadruple(this);
    }

}

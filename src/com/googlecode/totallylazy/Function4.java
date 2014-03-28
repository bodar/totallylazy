package com.googlecode.totallylazy;

public abstract class Function4<A, B, C, D, E> extends Function3<A, B, C, Function<D, E>> implements Callable4<A, B, C, D, E> {
    @Override
    public Function<D, E> call(final A a, final B b, final C c) throws Exception {
        return Functions.<A, B, C, D, E>apply(this, a).apply(b).apply(c);
    }

    public E apply(final A a, final B b, final C c, final D d){
        return Functions.call(this, a, b, c, d);
    }

    public Function<Quadruple<A, B, C, D>, E> quadruple() {
        return Functions.quadruple(this);
    }

}

package com.googlecode.totallylazy;

public abstract class Function3<A, B, C, D> extends Function2<A, B, Function<C, D>> implements Callable3<A, B, C, D> {
    @Override
    public Function<C, D> call(final A a, final B b) throws Exception {
        return Functions.<A, B, C, D>apply(this, a).apply(b);
    }

    public D apply(final A a, final B b, final C c) {
        return Functions.call(this, a, b, c);
    }

    public Function<Triple<A, B, C>, D> triple() {
        return Functions.triple(this);
    }
}

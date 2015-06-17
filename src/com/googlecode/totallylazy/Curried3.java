package com.googlecode.totallylazy;

public abstract class Curried3<A, B, C, D> extends Curried2<A, B, Function1<C, D>> implements Function3<A, B, C, D> {
    @Override
    public Function1<C, D> call(final A a, final B b) throws Exception {
        return c -> call(a,b,c);
    }

    public D apply(final A a, final B b, final C c) {
        return Functions.call(this, a, b, c);
    }

    public Function1<Triple<A, B, C>, D> triple() {
        return Functions.triple(this);
    }
}

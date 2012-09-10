package com.googlecode.totallylazy;


public abstract class Function2<A, B, C> extends Function1<A, Function1<B, C>> implements Callable2<A, B, C> {
    @Override
    public Function1<B, C> call(final A a) throws Exception {
        return Functions.apply(this, a);
    }

    public C apply(final A a, final B b) {
        return Functions.call(this, a, b);
    }

    public Function<C> deferApply(final A a, final B b) {
        return Callables.deferApply(this, a, b);
    }

    public Function2<B, A, C> flip() {
        return Callables.flip(this);
    }

    public Function1<Pair<A, B>, C> pair() {
        return Callables.pair(this);
    }

}

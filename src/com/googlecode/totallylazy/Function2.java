package com.googlecode.totallylazy;


public abstract class Function2<A, B, C> extends Function1<A, Function1<B, C>> implements Callable2<A,B,C> {
    @Override
    public Function1<B, C> call(final A a) throws Exception {
        return Callables.curry((Callable2<A, B, C>) this, a);
    }

    public C apply(final A a, final B b){
        return Callers.call(this, a, b);
    }

    public Function2<B,A,C> flip() {
        return Callables.flip(this);
    }
}

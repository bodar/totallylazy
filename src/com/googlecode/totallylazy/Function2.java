package com.googlecode.totallylazy;


public abstract class Function2<A, B, C> extends Function1<A, Function1<B, C>> implements Callable2<A,B,C> {
    public static <A, B, C> Function2<A, B, C> function(final Callable2<A, B, C> callable) {
        return new Function2<A, B, C>() {
            @Override
            public C call(A a, B b) throws Exception {
                return callable.call(a, b);
            }
        };
    }

    @Override
    public Function1<B, C> call(final A a) throws Exception {
        return Callables.curry((Callable2<A, B, C>) this, a);
    }

    public C apply(final A a, final B b){
        return Callers.call(this, a, b);
    }

    public Function<C> curry(final A a, final B b) {
        return Callables.curry(this, a, b);
    }

    public Function2<B,A,C> flip() {
        return Callables.flip(this);
    }

    public Function1<Pair<A,B>, C> paired() {
        return Callables.paired(this);
    }
}

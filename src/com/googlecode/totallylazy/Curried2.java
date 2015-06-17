package com.googlecode.totallylazy;


public abstract class Curried2<A, B, C> extends Function<A, Function<B, C>> implements Function2<A, B, C> {
    @Override
    public Function<B, C> call(final A a) throws Exception {
        return Functions.apply(this, a);
    }

    public C apply(final A a, final B b) {
        return Functions.call(this, a, b);
    }

    public Function<A, C> applySecond(final B b) {
        return flip().apply(b);
    }

    public Function0<C> deferApply(final A a, final B b) {
        return Callables.deferApply(this, a, b);
    }

    public Curried2<B, A, C> flip() {
        return Callables.flip(this);
    }

    public Function<Pair<A, B>, C> pair() {
        return Callables.pair(this);
    }

    public <D, E> Curried3<A, B, D, E> then(final Function2<? super C, ? super D, ? extends E> callable) {
        return new Curried3<A, B, D, E>() {
            @Override
            public E call(A a, B b, D d) throws Exception {
                return callable.call(Curried2.this.call(a, b), d);
            }
        };
    }
}

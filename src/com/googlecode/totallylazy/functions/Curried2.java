package com.googlecode.totallylazy.functions;


public interface Curried2<A, B, C> extends Function1<A, Function1<B, C>>, Function2<A, B, C> {
    @Override
    default Function1<B, C> call(final A a) throws Exception {
        return b -> call(a,b);
    }

    @Override
    default Curried2<B, A, C> flip() {
        return Callables.flip(this);
    }

    default <D, E> Curried3<A, B, D, E> then(final Function2<? super C, ? super D, ? extends E> callable) {
        return (a, b, d) -> callable.call(Curried2.this.call(a, b), d);
    }
}

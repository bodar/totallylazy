package com.googlecode.totallylazy;


public interface CurriedFunction2<A, B, C> extends Function1<A, Function1<B, C>>, Function2<A, B, C> {
    @Override
    default Function1<B, C> call(final A a) throws Exception {
        return b -> call(a,b);
    }

    @Override
    default CurriedFunction2<B, A, C> flip() {
        return Callables.flip(this);
    }

    default <D, E> CurriedFunction3<A, B, D, E> then(final Function2<? super C, ? super D, ? extends E> callable) {
        return (a, b, d) -> callable.call(CurriedFunction2.this.call(a, b), d);
    }
}

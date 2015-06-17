package com.googlecode.totallylazy;


public abstract class Curried2<A, B, C> implements Function1<A, Function1<B, C>>, Function2<A, B, C> {
    @Override
    public Function1<B, C> call(final A a) throws Exception {
        return b -> call(a,b);
    }

    @Override
    public Curried2<B, A, C> flip() {
        return Callables.flip(this);
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

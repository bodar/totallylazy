package com.googlecode.totallylazy;

public abstract class Function3<A, B, C, D> implements Callable3<A, B, C, D> {
    public Function<A, Function<B,  Function<C, D>>> curry() {
        return (a) -> (b) -> (c) -> call(a, b, c);
    }
    
    public BiFunction<B, C, D> apply(final A a) {
        return (b, c) -> apply(a, b, c);
    }

    public Function<C, D> apply(final A a, final B b) {
        return apply(a).apply(b);
    }

    public D apply(final A a, final B b, final C c) {
        return Functions.call(this, a, b, c);
    }

    public Function<Triple<A, B, C>, D> triple() {
        return Functions.triple(this);
    }
}

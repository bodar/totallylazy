package com.googlecode.totallylazy;

public interface Function3<A, B, C, D> extends Function2<A, B, Function<C, D>> {
    D call(A a, B b, C c) throws Exception;

    @Override
    default Function<C, D> call(final A a, final B b) throws Exception {
        return Functions.<A, B, C, D>apply(this, a).apply(b);
    }

    default D apply(final A a, final B b, final C c) {
        return Functions.call(this, a, b, c);
    }

    default Function<Triple<A, B, C>, D> triple() {
        return Functions.triple(this);
    }
}

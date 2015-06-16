package com.googlecode.totallylazy;

import static com.googlecode.totallylazy.LazyException.lazyException;

public interface Function3<A, B, C, D> extends Function2<A, B, Function1<C, D>> {
    D call(A a, B b, C c) throws Exception;

    @Override
    default Function1<C, D> call(final A a, final B b) throws Exception {
        return Functions.<A, B, C, D>apply(this, a).apply(b);
    }

    default D apply(final A a, final B b, final C c) {
        try {
            return call(a, b, c);
        } catch (Exception e) {
            throw lazyException(e);
        }
    }

    default Function1<Triple<A, B, C>, D> triple() {
        return Functions.triple(this);
    }
}

package com.googlecode.totallylazy;

import com.googlecode.totallylazy.functions.CurriedFunction2;
import com.googlecode.totallylazy.functions.Function1;

public class Lense<A, B> {
    private final Function1<A, B> get;
    private final CurriedFunction2<B, A, A> set;

    private Lense(Function1<A, B> get, CurriedFunction2<B, A, A> set) {
        this.get = get;
        this.set = set;
    }

    public static <A, B> Lense<A, B> lense(Function1<A, B> get, CurriedFunction2<B, A, A> set) {
        return new Lense<>(get, set);
    }

    public B get(A a) {
        return get.apply(a);
    }

    public A set(B b, A a) {
        return set.apply(b, a);
    }

    public A modify(A a, Function1<B, B> updateFunction) throws Exception {
        return set(updateFunction.call(get(a)), a);
    }

    public <C> Lense<A, C> then(final Lense<B, C> other) {
        return lense(get.then(other.get), setter(other));
    }

    private <C> CurriedFunction2<C, A, A> setter(final Lense<B, C> other) {
        return (c, a) -> set(other.set(c, get(a)), a);
    }
}
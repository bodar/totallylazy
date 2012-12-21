package com.googlecode.totallylazy;

public class Lense<A, B> {
    private final Function1<A, B> get;
    private final Function2<B, A, A> set;

    private Lense(Function1<A, B> get, Function2<B, A, A> set) {
        this.get = get;
        this.set = set;
    }

    public static <A, B> Lense<A, B> lense(Function1<A, B> get, Function2<B, A, A> set) {
        return new Lense<A, B>(get, set);
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

    private <C> Function2<C, A, A> setter(final Lense<B, C> other) {
        return new Function2<C,A,A>() {
            public A call(final C c, final A a) throws Exception {
                return set(other.set(c, get(a)), a);
            }
        };
    }
}
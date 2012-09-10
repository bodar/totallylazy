package com.googlecode.totallylazy;

public class Lens<A, B> extends Function1<A, B> {
    private final Function1<A, B> get;
    private final Function2<A, B, A> set;

    public Lens(Function1<A, B> get, Function2<A, B, A> set) {
        this.get = get;
        this.set = set;
    }

    public static <A, B> Lens<A, B> lens(Function1<A, B> getFunction, Function2<A, B, A> setFunction) {
        return new Lens<A, B>(getFunction, setFunction);
    }

    public B get(A whole) throws Exception {
        return get.call(whole);
    }

    public A set(A whole, B newValue) throws Exception {
        return set.call(whole, newValue);
    }

    public B call(A whole) throws Exception {
        return get(whole);
    }

    public A modify(A whole, Function1<B, B> updateFunction) throws Exception {
        return set(whole, updateFunction.call(get(whole)));
    }

    public <C> Lens<A, C> then(final Lens<B, C> other) {
        return other.compose(this);
    }

    public <C> Lens<C, B> compose(final Lens<C, A> other) {
        return new Lens<C, B>(
                functions.get(other).then(functions.get(this)),
                setter(other)
        );
    }

    private <C> Function2<C, B, C> setter(final Lens<C, A> other) {
        return new Function2<C, B, C>() {
            public C call(final C otherPart, final B part) throws Exception {
                return other.set(otherPart, set(other.get(otherPart), part));
            }
        };
    }

    public static class functions {
        public static <A, B> Function1<A, B> get(final Lens<A, B> lens) {
            return new Function1<A, B>() {
                public B call(A src) throws Exception {
                    return lens.get(src);
                }
            };
        }
    }
}
package com.googlecode.totallylazy;


public abstract class Function2<A, B, C> extends Function1<A, Function1<B, C>> implements Callable2<A, B, C> {
    public static <A, B, C> Function1<A, Function1<B, C>> curry(final Callable2<? super A, ? super B, ? extends C> callable) {
        return function(callable);
    }

    public static <A, B, C> Function2<A, B, C> function(final Callable2<? super A, ? super B, ? extends C> callable) {
        return new Function2<A, B, C>() {
            @Override
            public C call(A a, B b) throws Exception {
                return callable.call(a, b);
            }
        };
    }

    @Override
    public Function1<B, C> call(final A a) throws Exception {
        return apply(this, a);
    }

    public static <A, B, C> Function1<B, C> apply(final Callable2<? super A, ? super B, ? extends C> callable, final A value) {
        return new Function1<B, C>() {
            @Override
            public C call(B b) throws Exception {
                return callable.call(value, b);
            }
        };
    }

    public C apply(final A a, final B b) {
        return Callers.call(this, a, b);
    }

    public static <A, B, C> Function2<A, B, C> uncurry2(final Callable1<? super A, ? extends Callable1<? super B, ? extends C>> callable) {
        return new Function2<A, B, C>() {
            public final C call(final A a, final B b) throws Exception {
                return callable.call(a).call(b);
            }
        };
    }

    public Function<C> deferApply(final A a, final B b) {
        return Callables.deferApply(this, a, b);
    }

    public Function2<B, A, C> flip() {
        return Callables.flip(this);
    }

    public Function1<Pair<A, B>, C> pair() {
        return Callables.pair(this);
    }

    public static <A, B, C> Function2<A, B, C> returns2(final C result) {
        return new Function2<A, B, C>() {
            @Override
            public C call(A ignore, B ignoreMeToo) throws Exception {
                return result;
            }
        };
    }
}

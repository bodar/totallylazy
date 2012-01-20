package com.googlecode.totallylazy;

public abstract class Function3<A, B, C, D> extends Function2<A, B, Function1<C, D>> implements Callable3<A, B, C, D> {
    public static <A, B, C, D> Function1<A, Function1<B, Function1<C, D>>> curry(final Callable3<? super A, ? super B, ? super C, ? extends D> callable) {
        return function(callable);
    }

    public static <A, B, C, D> Function3<A, B, C, D> function(final Callable3<? super A, ? super B, ? super C, ? extends D> callable) {
        return new Function3<A, B, C, D>() {
            @Override
            public D call(A a, B b, C c) throws Exception {
                return callable.call(a, b, c);
            }
        };
    }

    @Override
    public Function1<C, D> call(final A a, final B b) throws Exception {
        return Function3.<A, B, C, D>apply(this, a).apply(b);
    }

    public static <A, B, C, D> Function2<B, C, D> apply(final Callable3<? super A, ? super B, ? super C, ? extends D> callable, final A value) {
        return new Function2<B, C, D>() {
            @Override
            public D call(B b, C c) throws Exception {
                return callable.call(value, b, c);
            }
        };
    }

    public D apply(final A a, final B b, final C c){
        return Callers.call(this, a, b, c);
    }

    public static <A, B, C, D> Function3<A, B, C, D> uncurry3(final Callable1<? super A, ? extends Callable1<? super B, ? extends Callable1<? super C, ? extends D>>> callable) {
        return new Function3<A, B, C, D>() {
            @Override
            public D call(A a, B b, C c) throws Exception {
                return callable.call(a).call(b).call(c);
            }
        };
    }

    public Function1<Triple<A, B, C>, D> triple() {
        return Callables.triple(this);
    }
}

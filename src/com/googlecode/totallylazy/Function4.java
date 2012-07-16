package com.googlecode.totallylazy;

import static com.googlecode.totallylazy.LazyException.lazyException;

public abstract class Function4<A, B, C, D, E> extends Function3<A, B, C, Function1<D, E>> implements Callable4<A, B, C, D, E> {
    public static <A, B, C, D, E> Function4<A, B, C, D, E> function(final Callable4<? super A, ? super B, ? super C, ? super D, ? extends E> callable) {
        return new Function4<A, B, C, D, E>() {
            @Override
            public E call(A a, B b, C c, D d) throws Exception {
                return callable.call(a, b, c, d);
            }
        };
    }

    @Override
    public Function1<D, E> call(final A a, final B b, final C c) throws Exception {
        return Function4.<A, B, C, D, E>apply(this, a).apply(b).apply(c);
    }

    public static <A, B, C, D, E> Function3<B, C, D, E> apply(final Callable4<? super A, ? super B, ? super C, ? super D, ? extends E> callable, final A value) {
        return new Function3<B, C, D, E>() {
            @Override
            public E call(B b, C c, D d) throws Exception {
                return callable.call(value, b, c, d);
            }
        };
    }

    public E apply(final A a, final B b, final C c, final D d){
        return Function4.<A,B,C,D,E>call(this, a, b, c, d);
    }

    public static <A, B, C, D, E> E call(final Callable4<? super A, ? super B, ? super C, ? super D, ? extends E> callable, final A a, final B b, final C c, final D d) {
        try {
            return callable.call(a, b, c, d);
        } catch (Exception e) {
            throw lazyException(e);
        }
    }

    public static <A, B, C, D, E> Function4<A, B, C, D, E> uncurry4(final Callable1<? super A, ? extends Callable1<? super B, ? extends Callable1<? super C, ? extends Callable1<? super D, ? extends E>>>> callable) {
        return new Function4<A, B, C, D, E>() {
            @Override
            public E call(A a, B b, C c, D d) throws Exception {
                return callable.call(a).call(b).call(c).call(d);
            }
        };
    }

    public Function1<Quadruple<A, B, C, D>, E> quadruple() {
        return Function4.<A,B,C,D, E>quadruple(this);
    }

    public static <A, B, C, D, E> Function1<Quadruple<A, B, C, D>, E> quadruple(final Callable4<? super A, ? super B, ? super C, ? super D, ? extends E> callable) {
        return new Function1<Quadruple<A, B, C, D>, E>() {
            @Override
            public E call(Quadruple<A, B, C, D> quadruple) throws Exception {
                return callable.call(quadruple.first(), quadruple.second(), quadruple.third(), quadruple.fourth());
            }
        };
    }

    public static <A, B, C, D, E> Function4<A, B, C, D, E> unquadruple(final Callable1<? super Quadruple<? extends A, ? extends B, ? extends C, ? extends D>, ? extends E> function) {
        return new Function4<A, B, C, D, E>() {
            @Override
            public E call(A a, B b, C c, D d) throws Exception {
                return function.call(Quadruple.quadruple(a, b, c, d));
            }
        };
    }
}

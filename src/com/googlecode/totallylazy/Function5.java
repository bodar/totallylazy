package com.googlecode.totallylazy;

import static com.googlecode.totallylazy.LazyException.lazyException;

public abstract class Function5<A, B, C, D, E, F> extends Function4<A, B, C, D, Function1<E, F>> implements Callable5<A, B, C, D, E, F> {
    public static <A, B, C, D, E, F> Function5<A, B, C, D, E, F> function(final Callable5<? super A, ? super B, ? super C, ? super D, ? super E, ? extends F> callable) {
        return new Function5<A, B, C, D, E, F>() {
            @Override
            public F call(A a, B b, C c, D d, E e) throws Exception {
                return callable.call(a, b, c, d, e);
            }
        };
    }

    @Override
    public Function1<E, F> call(final A a, final B b, final C c, final D d) throws Exception {
        return Function5.<A, B, C, D, E, F>apply(this, a).apply(b).apply(c).apply(d);
    }

    public static <A, B, C, D, E, F> Function4<B, C, D, E, F> apply(final Callable5<? super A, ? super B, ? super C, ? super D, ? super E, ? extends F> callable, final A value) {
        return new Function4<B, C, D, E, F>() {
            @Override
            public F call(B b, C c, D d, E e) throws Exception {
                return callable.call(value, b, c, d, e);
            }
        };
    }

    public F apply(final A a, final B b, final C c, final D d, final E e) {
        return Function5.<A, B, C, D, E, F>call(this, a, b, c, d, e);
    }

    public static <A, B, C, D, E, F> F call(final Callable5<? super A, ? super B, ? super C, ? super D, ? super E, ? extends F> callable, final A a, final B b, final C c, final D d, final E e) {
        try {
            return callable.call(a, b, c, d, e);
        } catch (Exception ex) {
            throw lazyException(ex);
        }
    }

    public static <A, B, C, D, E, F> Function5<A, B, C, D, E, F> uncurry4(final Callable1<? super A, ? extends Callable1<? super B, ? extends Callable1<? super C, ? extends Callable1<? super D, ? extends Callable1<? super E, ? extends F>>>>> callable) {
        return new Function5<A, B, C, D, E, F>() {
            @Override
            public F call(A a, B b, C c, D d, E e) throws Exception {
                return callable.call(a).call(b).call(c).call(d).call(e);
            }
        };
    }

    public Function1<Quintuple<A, B, C, D, E>, F> quintuple() {
        return Function5.<A, B, C, D, E, F>quintuple(this);
    }

    public static <A, B, C, D, E, F> Function1<Quintuple<A, B, C, D, E>, F> quintuple(final Callable5<? super A, ? super B, ? super C, ? super D, ? super E, ? extends F> callable) {
        return new Function1<Quintuple<A, B, C, D, E>, F>() {
            @Override
            public F call(Quintuple<A, B, C, D, E> quintuple) throws Exception {
                return callable.call(quintuple.first(), quintuple.second(), quintuple.third(), quintuple.fourth(), quintuple.fifth());
            }
        };
    }

    public static <A, B, C, D, E, F> Function5<A, B, C, D, E, F> unquintuple(final Callable1<? super Quintuple<? extends A, ? extends B, ? extends C, ? extends D, ? extends E>, ? extends F> function) {
        return new Function5<A, B, C, D, E, F>() {
            @Override
            public F call(A a, B b, C c, D d, E e) throws Exception {
                return function.call(Quintuple.quintuple(a, b, c, d, e));
            }
        };
    }
}

package com.googlecode.totallylazy;

import com.googlecode.totallylazy.callables.And;
import com.googlecode.totallylazy.callables.Or;
import com.googlecode.totallylazy.callables.Xor;

import java.util.concurrent.Callable;

import static com.googlecode.totallylazy.LazyException.lazyException;

public class Functions {
    public static <A> Function<A> function(final Callable<? extends A> callable) {
        return new Function<A>() {
            @Override
            public A call() throws Exception {
                return callable.call();
            }
        };
    }

    public static <A, B> Function1<A, B> function(final Callable1<? super A, ? extends B> callable) {
        return new Function1<A, B>() {
            @Override
            public B call(A a) throws Exception {
                return callable.call(a);
            }
        };
    }

    public static <A, B, C> Function2<A, B, C> function(final Callable2<? super A, ? super B, ? extends C> callable) {
        return new Function2<A, B, C>() {
            @Override
            public C call(A a, B b) throws Exception {
                return callable.call(a, b);
            }
        };
    }

    public static <A, B, C, D> Function3<A, B, C, D> function(final Callable3<? super A, ? super B, ? super C, ? extends D> callable) {
        return new Function3<A, B, C, D>() {
            @Override
            public D call(A a, B b, C c) throws Exception {
                return callable.call(a, b, c);
            }
        };
    }

    public static <A, B, C, D, E> Function4<A, B, C, D, E> function(final Callable4<? super A, ? super B, ? super C, ? super D, ? extends E> callable) {
        return new Function4<A, B, C, D, E>() {
            @Override
            public E call(A a, B b, C c, D d) throws Exception {
                return callable.call(a, b, c, d);
            }
        };
    }

    public static <A, B, C, D, E, F> Function5<A, B, C, D, E, F> function(final Callable5<? super A, ? super B, ? super C, ? super D, ? super E, ? extends F> callable) {
        return new Function5<A, B, C, D, E, F>() {
            @Override
            public F call(A a, B b, C c, D d, E e) throws Exception {
                return callable.call(a, b, c, d, e);
            }
        };
    }

    public static <A> A call(final Callable<? extends A> callable) {
        try {
            return callable.call();
        } catch (Exception e) {
            throw lazyException(e);
        }
    }

    public static <A, B> B call(final Callable1<? super A, ? extends B> callable, final A a) {
        try {
            return callable.call(a);
        } catch (Exception e) {
            throw lazyException(e);
        }
    }

    public static <A, B, C> C call(final Callable2<? super A, ? super B, ? extends C> callable, final A a, final B b) {
        try {
            return callable.call(a, b);
        } catch (Exception e) {
            throw lazyException(e);
        }
    }

    public static <A, B, C, D> D call(final Callable3<? super A, ? super B, ? super C, ? extends D> callable, final A a, final B b, final C c) {
        try {
            return callable.call(a, b, c);
        } catch (Exception e) {
            throw lazyException(e);
        }
    }

    public static <A, B, C, D, E> E call(final Callable4<? super A, ? super B, ? super C, ? super D, ? extends E> callable, final A a, final B b, final C c, final D d) {
        try {
            return callable.call(a, b, c, d);
        } catch (Exception e) {
            throw lazyException(e);
        }
    }

    public static <A, B, C, D, E, F> F call(final Callable5<? super A, ? super B, ? super C, ? super D, ? super E, ? extends F> callable, final A a, final B b, final C c, final D d, final E e) {
        try {
            return callable.call(a, b, c, d, e);
        } catch (Exception ex) {
            throw lazyException(ex);
        }
    }

    public static <A, B, C> Function1<B, C> apply(final Callable2<? super A, ? super B, ? extends C> callable, final A value) {
        return new Function1<B, C>() {
            @Override
            public C call(B b) throws Exception {
                return callable.call(value, b);
            }
        };
    }

    public static <A, B, C, D> Function2<B, C, D> apply(final Callable3<? super A, ? super B, ? super C, ? extends D> callable, final A value) {
        return new Function2<B, C, D>() {
            @Override
            public D call(B b, C c) throws Exception {
                return callable.call(value, b, c);
            }
        };
    }

    public static <A, B, C, D, E> Function3<B, C, D, E> apply(final Callable4<? super A, ? super B, ? super C, ? super D, ? extends E> callable, final A value) {
        return new Function3<B, C, D, E>() {
            @Override
            public E call(B b, C c, D d) throws Exception {
                return callable.call(value, b, c, d);
            }
        };
    }

    public static <A, B, C, D, E, F> Function4<B, C, D, E, F> apply(final Callable5<? super A, ? super B, ? super C, ? super D, ? super E, ? extends F> callable, final A value) {
        return new Function4<B, C, D, E, F>() {
            @Override
            public F call(B b, C c, D d, E e) throws Exception {
                return callable.call(value, b, c, d, e);
            }
        };
    }

    public static <A> Function1<A, A> identity() {
        return new Function1<A, A>() {
            public A call(A self) throws Exception {
                return self;
            }
        };
    }

    public static <A> Function1<A, A> identity(Class<A> aClass) {
        return identity();
    }

    public static <A, B> Function1<A, B> constant(final B result) {
        return new Function1<A, B>() {
            public B call(A ignore) throws Exception {
                return result;
            }
        };
    }

    public static <T> Function<T> returns(final T t) {
        return new Function<T>() {
            public final T call() throws Exception {
                return t;
            }
        };
    }

    public static <A, B> Function1<A, B> returns1(final B result) {
        return constant(result);
    }

    public static <A, B, C> Function2<A, B, C> returns2(final C result) {
        return new Function2<A, B, C>() {
            @Override
            public C call(A ignore, B ignoreMeToo) throws Exception {
                return result;
            }
        };
    }

    public static <A, B, C> Function2<A, B, C> uncurry2(final Callable1<? super A, ? extends Callable1<? super B, ? extends C>> callable) {
        return new Function2<A, B, C>() {
            public final C call(final A a, final B b) throws Exception {
                return callable.call(a).call(b);
            }
        };
    }

    public static <A, B, C, D> Function3<A, B, C, D> uncurry3(final Callable1<? super A, ? extends Callable1<? super B, ? extends Callable1<? super C, ? extends D>>> callable) {
        return new Function3<A, B, C, D>() {
            @Override
            public D call(A a, B b, C c) throws Exception {
                return callable.call(a).call(b).call(c);
            }
        };
    }

    public static <A, B, C, D, E> Function4<A, B, C, D, E> uncurry4(final Callable1<? super A, ? extends Callable1<? super B, ? extends Callable1<? super C, ? extends Callable1<? super D, ? extends E>>>> callable) {
        return new Function4<A, B, C, D, E>() {
            @Override
            public E call(A a, B b, C c, D d) throws Exception {
                return callable.call(a).call(b).call(c).call(d);
            }
        };
    }

    public static <A, B, C, D, E, F> Function5<A, B, C, D, E, F> uncurry5(final Callable1<? super A, ? extends Callable1<? super B, ? extends Callable1<? super C, ? extends Callable1<? super D, ? extends Callable1<? super E, ? extends F>>>>> callable) {
        return new Function5<A, B, C, D, E, F>() {
            @Override
            public F call(A a, B b, C c, D d, E e) throws Exception {
                return callable.call(a).call(b).call(c).call(d).call(e);
            }
        };
    }

    public static <A, B, C> Function1<Pair<A, B>, C> pair(final Callable2<? super A, ? super B, ? extends C> function) {
        return new Function1<Pair<A, B>, C>() {
            @Override
            public C call(Pair<A, B> pair) throws Exception {
                return function.call(pair.first(), pair.second());
            }
        };
    }

    public static <A, B, C> Function2<A, B, C> unpair(final Callable1<? super Pair<? extends A, ? extends B>, ? extends C> function) {
        return new Function2<A, B, C>() {
            @Override
            public C call(A a, B b) throws Exception {
                return function.call(Pair.pair(a, b));
            }
        };
    }

    public static <A, B, C, D>Function1<Triple<A, B, C>, D> triple(final Callable3<? super A, ? super B, ? super C, ? extends D> callable) {
        return new Function1<Triple<A, B, C>, D>() {
            @Override
            public D call(Triple<A, B, C> triple) throws Exception {
                return callable.call(triple.first(), triple.second(), triple.third());
            }
        };
    }

    public static <A, B, C, D> Function3<A, B, C, D> untriple(final Callable1<? super Triple<? extends A, ? extends B, ? extends C>, ? extends D> function) {
        return new Function3<A, B, C, D>() {
            @Override
            public D call(A a, B b, C c) throws Exception {
                return function.call(Triple.triple(a, b, c));
            }
        };
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

    public static Function2<Boolean, Boolean, Boolean> and = new And();
    public static Function2<Boolean, Boolean, Boolean> or = new Or();
    public static Function2<Boolean, Boolean, Boolean> xor = new Xor();

    public static Function1<Pair<Boolean, Boolean>, Boolean> andPair() {
        return new Function1<Pair<Boolean, Boolean>, Boolean>() {
            @Override
            public Boolean call(Pair<Boolean, Boolean> pair) throws Exception {
                return pair.first() && pair.second();
            }
        };
    }

    public static Function1<Pair<Boolean, Boolean>, Boolean> orPair() {
        return new Function1<Pair<Boolean, Boolean>, Boolean>() {
            @Override
            public Boolean call(Pair<Boolean, Boolean> pair) throws Exception {
                return pair.first() || pair.second();
            }
        };
    }
}

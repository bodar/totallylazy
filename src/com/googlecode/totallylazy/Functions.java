package com.googlecode.totallylazy;

import com.googlecode.totallylazy.callables.And;
import com.googlecode.totallylazy.callables.Or;
import com.googlecode.totallylazy.callables.Xor;

import java.util.List;
import java.util.concurrent.Callable;

import static com.googlecode.totallylazy.LazyException.lazyException;
import static com.googlecode.totallylazy.Sequences.sequence;

public class Functions {
    public static <A> Function0<A> function(final Callable<? extends A> callable) {
        return new Function0<A>() {
            @Override
            public A call() throws Exception {
                return callable.call();
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

    public static <A, B, C, D, E, F> F call(final Function5<? super A, ? super B, ? super C, ? super D, ? super E, ? extends F> callable, final A a, final B b, final C c, final D d, final E e) {
        try {
            return callable.call(a, b, c, d, e);
        } catch (Exception ex) {
            throw lazyException(ex);
        }
    }

    public static <A, B, C, D> Function2<B, C, D> apply(final Function3<? super A, ? super B, ? super C, ? extends D> callable, final A value) {
        return (b, c) -> callable.call(value, b, c);
    }

    public static <A, B, C, D, E> Function3<B, C, D, E> apply(final Function4<? super A, ? super B, ? super C, ? super D, ? extends E> callable, final A value) {
        return (b, c, d) -> callable.call(value, b, c, d);
    }

    public static <A, B, C, D, E, F> Function4<B, C, D, E, F> apply(final Function5<? super A, ? super B, ? super C, ? super D, ? super E, ? extends F> callable, final A value) {
        return (b, c, d, e) -> callable.call(value, b, c, d, e);
    }

    public static <A> UnaryFunction<A> identity() {
        return self -> self;
    }

    public static <A> UnaryFunction<A> identity(Class<A> aClass) {
        return identity();
    }

    public static <A, B> Function1<A, B> constant(final B result) {
        return ignore -> result;
    }

    public static <T> Function0<T> returns(final T t) {
        return () -> t;
    }

    public static <A, B> Function1<A, B> returns1(final B result) {
        return constant(result);
    }

    public static <A, B, C> Function2<A, B, C> returns2(final C result) {
        return (ignore, ignoreMeToo) -> result;
    }

    public static <A, B, C> Function2<A, B, C> uncurry2(final Function1<? super A, ? extends Function1<? super B, ? extends C>> callable) {
        return (a, b) -> callable.call(a).call(b);
    }

    public static <A, B, C, D> Function3<A, B, C, D> uncurry3(final Function1<? super A, ? extends Function1<? super B, ? extends Function1<? super C, ? extends D>>> callable) {
        return (a, b, c) -> callable.call(a).call(b).call(c);
    }

    public static <A, B, C, D, E> Function4<A, B, C, D, E> uncurry4(final Function1<? super A, ? extends Function1<? super B, ? extends Function1<? super C, ? extends Function1<? super D, ? extends E>>>> callable) {
        return (a, b, c, d) -> callable.call(a).call(b).call(c).call(d);
    }

    public static <A, B, C, D, E, F> Function5<A, B, C, D, E, F> uncurry5(final Function1<? super A, ? extends Function1<? super B, ? extends Function1<? super C, ? extends Function1<? super D, ? extends Function1<? super E, ? extends F>>>>> callable) {
        return (a, b, c, d, e) -> callable.call(a).call(b).call(c).call(d).call(e);
    }

    static abstract class IdentityFunction<A,B> implements Function1<A,B>, Identity<B> {}

    public static <A, B, C> Function1<Pair<A, B>, C> pair(final Function2<? super A, ? super B, ? extends C> function) {
        if(function instanceof Identity) {
            return new IdentityFunction<Pair<A, B>, C>() {
                @Override
                public C identity() {
                    return Unchecked.<Identity<C>>cast(function).identity();
                }

                @Override
                public C call(Pair<A, B> pair) throws Exception {
                    return function.call(pair.first(), pair.second());
                }
            };
        }
        return pair -> function.call(pair.first(), pair.second());
    }

    public static <A, B, C> Function2<A, B, C> unpair(final Function1<? super Pair<? extends A, ? extends B>, ? extends C> function) {
        return (a, b) -> function.call(Pair.pair(a, b));
    }

    public static <A, B, C, D>Function1<Triple<A, B, C>, D> triple(final Function3<? super A, ? super B, ? super C, ? extends D> callable) {
        return triple -> callable.call(triple.first(), triple.second(), triple.third());
    }

    public static <A, B, C, D> Function3<A, B, C, D> untriple(final Function1<? super Triple<? extends A, ? extends B, ? extends C>, ? extends D> function) {
        return (a, b, c) -> function.call(Triple.triple(a, b, c));
    }

    public static <A, B, C, D, E> Function1<Quadruple<A, B, C, D>, E> quadruple(final Function4<? super A, ? super B, ? super C, ? super D, ? extends E> callable) {
        return quadruple -> callable.call(quadruple.first(), quadruple.second(), quadruple.third(), quadruple.fourth());
    }

    public static <A, B, C, D, E> Function4<A, B, C, D, E> unquadruple(final Function1<? super Quadruple<? extends A, ? extends B, ? extends C, ? extends D>, ? extends E> function) {
        return (a, b, c, d) -> function.call(Quadruple.quadruple(a, b, c, d));
    }

    public static <A, B, C, D, E, F> Function1<Quintuple<A, B, C, D, E>, F> quintuple(final Function5<? super A, ? super B, ? super C, ? super D, ? super E, ? extends F> callable) {
        return quintuple -> callable.call(quintuple.first(), quintuple.second(), quintuple.third(), quintuple.fourth(), quintuple.fifth());
    }

    public static <A, B, C, D, E, F> Function5<A, B, C, D, E, F> unquintuple(final Function1<? super Quintuple<? extends A, ? extends B, ? extends C, ? extends D, ? extends E>, ? extends F> function) {
        return (a, b, c, d, e) -> function.call(Quintuple.quintuple(a, b, c, d, e));
    }

    public static Monoid<Boolean> and = new And();
    public static Monoid<Boolean> or = new Or();
    public static Monoid<Boolean> xor = new Xor();

    public static Function1<Pair<Boolean, Boolean>, Boolean> andPair() {
        return pair -> pair.first() && pair.second();
    }

    public static Function1<Pair<Boolean, Boolean>, Boolean> orPair() {
        return pair -> pair.first() || pair.second();
    }

    public static <A, B> Function1<A, B> interruptable(final Function1<? super A, ? extends B> function) {
        return a -> {
            if (Thread.interrupted()) throw new InterruptedException();
            return function.call(a);
        };
    }

    public static <A> Function0<A> interruptable(final Callable<? extends A> function) {
        return () -> {
            if (Thread.interrupted()) throw new InterruptedException();
            return function.call();
        };
    }

    public static <A,B> Function1<A, Option<B>> option(Predicate<? super A> predicate, Function1<? super A, ? extends B> callable) {
        return a -> predicate.matches(a) ? Option.some(callable.call(a)) : Option.none();
    }

    public static <A, B extends A, C> Function1<A, Option<C>> instanceOf(Class<B> subCLass, Function1<? super B, ? extends C> callable) {
        return a -> subCLass.isInstance(a) ? Option.some(callable.call(subCLass.cast(a))) : Option.none();
    }

    @SafeVarargs
    public static <A,B> Function1<A, Option<B>> or(Function1<? super A, ? extends Option<B>>... callables) {
        return a -> sequence(callables).flatMap(fun -> fun.call(a)).headOption();
    }

    @SafeVarargs
    public static <A,B> Function1<A, List<B>> and(Function1<? super A, ? extends Option<B>>... callables) {
        return a -> {
            List<B> result = sequence(callables).flatMap(fun -> fun.call(a)).toList();
            if(result.size() != callables.length) return Lists.list();
            return result;
        };
    }

}

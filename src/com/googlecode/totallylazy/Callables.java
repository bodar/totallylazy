package com.googlecode.totallylazy;

import com.googlecode.totallylazy.comparators.Comparators;

import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.util.Comparator;
import java.util.Iterator;
import java.util.concurrent.Callable;

import static com.googlecode.totallylazy.Methods.method;
import static com.googlecode.totallylazy.Option.identity;
import static com.googlecode.totallylazy.Sequences.sequence;

public final class Callables {
    public static <T> Function1<Value<T>, T> value() {
        return value -> value.value();
    }

    public static <T> Function1<Value<T>, T> value(Class<T> aClass) {
        return value();
    }

    public static <T, R> Function1<T, R> asCallable1(final Callable<? extends R> callable) {
        return t -> callable.call();
    }

    public static <T> UnaryFunction<T> nullGuard(final Function1<? super T, ? extends T> callable) {
        return o -> {
            if (o == null) return null;
            return callable.call(o);
        };
    }

    public static <T> UnaryFunction<Sequence<T>> reduceAndShift(final Function2<? super T, ? super T, ? extends T> action) {
        return values -> values.tail().append(values.reduceLeft(action));
    }

    public static <T, S> Function1<T, S> cast(final Class<? extends S> aClass) {
        return t -> aClass.cast(t);
    }

    public static <T, S> Function1<T, S> cast() {
        return Unchecked::cast;
    }

    public static Function1<Object, Class<?>> toClass() {
        return o -> {
            if(o == null) return Void.class;
            return o.getClass();
        };
    }

    public static <T, R extends Comparable<? super R>> Comparator<T> ascending(final Function1<? super T, ? extends R> callable) {
        return Comparators.ascending(callable);
    }

    public static <T, R extends Comparable<? super R>> Comparator<T> descending(final Function1<? super T, ? extends R> callable) {
        return Comparators.descending(callable);
    }

    public static Function1<Object, Integer> size() {
        return length();
    }

    public static Function1<Object, Integer> length() {
        return instance -> {
            Class aClass = instance.getClass();
            if (aClass.isArray()) {
                return Array.getLength(instance);
            }
            return sequence(method(instance, "size"), method(instance, "length")).
                    flatMap(identity(Method.class)).
                    headOption().
                    map(Methods.<Integer>invokeOn(instance)).
                    getOrElse(Callables.<Integer>callThrows(new UnsupportedOperationException("Does not support fields yet")));
        };
    }


    public static <T> UnaryFunction<Sequence<T>> realise() {
        return Sequence<T>::realise;
    }

    public static <T> Function1<First<T>, T> first(Class<T> aClass) {
        return first();
    }

    public static <T> Function1<First<T>, T> first() {
        return First::first;
    }

    public static <T> Function1<Iterable<T>, T> last(Class<T> t) {
        return last();
    }

    public static <T> Function1<Iterable<T>, T> last() {
        return Sequences::last;
    }

    public static <F, S, R> Function1<Pair<F, S>, Pair<R, S>> first(final Function1<? super F, ? extends R> firstCallable, Class<S> sClass) {
        return first(firstCallable);
    }

    public static <F, S, R> Function1<Pair<F, S>, Pair<R, S>> first(final Function1<? super F, ? extends R> firstCallable) {
        return pair -> Pair.pair(firstCallable.call(pair.first()), pair.second());
    }

    public static <T> Function1<Second<T>, T> second(Class<T> aClass) {
        return second();
    }

    public static <T> Function1<Second<T>, T> second() {
        return second -> second.second();
    }

    public static <F, S, R> Function1<Pair<F, S>, Pair<F, R>> second(final Function1<? super S, ? extends R> secondCallable) {
        return pair -> Pair.pair(pair.first(), secondCallable.call(pair.second()));
    }

    public static <T> Function1<Third<T>, T> third(Class<T> aClass) {
        return third();
    }

    public static <T> Function1<Third<T>, T> third() {
        return third -> third.third();
    }

    public static <F, S, T, R> Function1<Triple<F, S, T>, Triple<F, S, R>> third(final Function1<? super T, ? extends R> thirdCallable) {
        return triple -> Triple.triple(triple.first(), triple.second(), thirdCallable.call(triple.third()));
    }

    public static <T> Function1<Fourth<T>, T> fourth(Class<T> aClass) {
        return fourth();
    }

    public static <T> Function1<Fourth<T>, T> fourth() {
        return fourth -> fourth.fourth();
    }

    public static <F, S, T, Fo, R> Function1<Quadruple<F, S, T, Fo>, Quadruple<F, S, T, R>> fourth(final Function1<? super Fo, ? extends R> fourthCallable) {
        return quadruple -> Quadruple.quadruple(quadruple.first(), quadruple.second(), quadruple.third(), fourthCallable.call(quadruple.fourth()));
    }

    public static <T> Function1<Fifth<T>, T> fifth(Class<T> aClass) {
        return fifth();
    }

    public static <T> Function1<Fifth<T>, T> fifth() {
        return fifth -> fifth.fifth();
    }

    public static <F, S, T, Fo, Fi, R> Function1<Quintuple<F, S, T, Fo, Fi>, Quintuple<F, S, T, Fo, R>> fifth(final Function1<? super Fi, ? extends R> callable) {
        return quintuple -> Quintuple.quintuple(quintuple.first(), quintuple.second(), quintuple.third(), quintuple.fourth(), callable.call(quintuple.fifth()));
    }

    public static <T> Function1<Iterable<T>, T> head() {
        return iterable -> Sequences.head(iterable);
    }

    public final static Function1<Object, String> toString = new Function1<Object, String>() {
        public final String call(final Object value) {
            return value != null ? value.toString() : null;
        }

        @Override
        public String toString() {
            return "toString";
        }
    };

    public static <T> Function1<Object, String> asString() {
        return toString;
    }

    public static final CurriedFunction2<Integer, Object, Integer> hashCode = (hash, value) -> {
        if (value == null) return hash * 19;
        int current = value.hashCode();
        return (current == 0 ? 19 : current) * hash;
    };

    public static CurriedFunction2<Integer, Object, Integer> asHashCode() {
        return hashCode;
    }


    public static <T> Function1<Iterable<T>, Iterator<T>> asIterator() {
        return iterable -> iterable.iterator();
    }

    public static <T> Function1<Iterator<T>, Iterable<T>> asIterable() {
        return iterator -> Sequences.forwardOnly(iterator);
    }

    public static <T> Function0<T> returns(final T t) {
        return Functions.returns(t);
    }

    public static <T, R> Function1<T, R> ignoreAndReturn(final R r) {
        return returns1(r);
    }

    public static <A, B> Function1<A, B> returns1(B result) {
        return Functions.returns1(result);
    }

    public static <A, B, C> CurriedFunction2<A, B, C> returns2(C result) {
        return Functions.returns2(result);
    }

    public static <T> UnaryFunction<T> returnArgument() {
        return Functions.identity();
    }

    public static <T> UnaryFunction<T> returnArgument(final Class<T> aClass) {
        return returnArgument();
    }

    public static <T> Function0<T> aNull(final Class<T> aClass) {
        return () -> null;
    }

    public static <T> Function0<T> callThrows(final Exception e) {
        return () -> {
            throw e;
        };
    }

    public static <T> Function0<T> callThrows(final Exception e, final Class<T> aClass) {
        return callThrows(e);
    }

    public static <T> Function1<Callable<T>, T> call() {
        return callable -> callable.call();
    }

    public static <T> Function1<Callable<T>, T> call(final Class<T> aClass) {
        return call();
    }

    public static <T, R> Function1<Function1<T, R>, R> callWith(final T value) {
        return callable -> callable.call(value);
    }

    public static <A, B, C> Function0<C> deferApply(final Function2<? super A, ? super B, ? extends C> callable, final A a, final B b) {
        return () -> callable.call(a, b);
    }

    public static <A, B> Function0<B> deferApply(final Function1<? super A, ? extends B> callable, final A value) {
        return () -> callable.call(value);
    }

    public static <A, B> Function1<A, Function0<B>> deferReturn(final Function1<? super A, ? extends B> callable) {
        return a -> deferApply(callable, a);
    }

    public static <A, B, C> Function1<B, C> apply(final Function2<? super A, ? super B, ? extends C> callable, final A value) {
        return Functions.apply(callable, value);
    }

    public static <A, B, C, D> CurriedFunction2<B, C, D> apply(final Function3<? super A, ? super B, ? super C, ? extends D> callable, final A value) {
        return Functions.apply(callable, value);
    }

    public static <A, B, C> Function1<A, Function1<B, C>> curry(final Function2<? super A, ? super B, ? extends C> callable) {
        return (CurriedFunction2<A,B,C>) callable::call;
    }

    public static <A, B, C, D> Function1<A, Function1<B, Function1<C, D>>> curry(final Function3<? super A, ? super B, ? super C, ? extends D> callable) {
        return (CurriedFunction3<A,B,C,D>) callable::call;
    }

    public static <A, B, C> CurriedFunction2<A, B, C> uncurry2(final Function1<? super A, ? extends Function1<? super B, ? extends C>> callable) {
        return Functions.uncurry2(callable);
    }

    public static <A, B, C, D> CurriedFunction3<A, B, C, D> uncurry3(final Function1<? super A, ? extends Function1<? super B, ? extends Function1<? super C, ? extends D>>> callable) {
        return Functions.uncurry3(callable);
    }

    public static <L> Function1<Either<L, ?>, L> left(Class<L> aClass) {
        return left();
    }

    public static <L> Function1<Either<L, ?>, L> left() {
        return either -> either.left();
    }

    public static <R> Function1<Either<?, R>, R> right(Class<R> aClass) {
        return right();
    }

    public static <R> Function1<Either<?, R>, R> right() {
        return either -> either.right();
    }

    public static <A, B, C> CurriedFunction2<B, A, C> flip(final Function2<? super A, ? super B, ? extends C> callable) {
        return (s, t) -> callable.call(t, s);
    }

    public static <A, B, C> Function1<A, C> compose(final Function1<? super A, ? extends B> first, final Function1<? super B, ? extends C> second) {
        return a -> second.call(first.call(a));
    }

    public static <A, B> Function1<A, B> compose(final Function1<? super A, ?> ignoreResult, final Callable<? extends B> callable) {
        return a -> {
            ignoreResult.call(a);
            return callable.call();
        };
    }

    public static <A, B> Function1<A, B> doThen(final Function1<? super A, ?> ignoreResult, final Callable<? extends B> callable) {
        return compose(ignoreResult, callable);
    }

    public static <A, B> Function0<B> compose(final Callable<? extends A> first, final Function1<? super A, ? extends B> second) {
        return () -> second.call(first.call());
    }


    public static <A, B> Function1<A, B> interruptable(final Function1<? super A, ? extends B> function) {
        return Functions.interruptable(function);
    }

    public static <A, B, C> Function1<Pair<A, B>, C> pair(final Function2<? super A, ? super B, ? extends C> function) {
        return Functions.pair(function);
    }

    public static <A, B, C> CurriedFunction2<A, B, C> unpair(final Function1<? super Pair<? extends A, ? extends B>, ? extends C> function) {
        return Functions.unpair(function);
    }

    public static <A, B, C, D> Function1<Triple<A, B, C>, D> triple(final Function3<? super A, ? super B, ? super C, ? extends D> callable) {
        return Functions.triple(callable);
    }

    public static <A, B, C, D> CurriedFunction3<A, B, C, D> untriple(final Function1<? super Triple<? extends A, ? extends B, ? extends C>, ? extends D> callable) {
        return Functions.untriple(callable);
    }

    public static <L, R> Function1<L, Either<L, R>> asLeft() {
        return Either.functions.asLeft();
    }

    public static <L, R> Function1<R, Either<L, R>> asRight() {
        return Either.functions.asRight();
    }

    public static <T> UnaryFunction<T> replace(final Predicate<? super T> predicate, final Function1<? super T, ? extends T> callable) {
        return when(predicate, callable);
    }

    public static <T> UnaryFunction<T> when(final Predicate<? super T> predicate, final Function1<? super T, ? extends T> callable) {
        return value -> predicate.matches(value) ? callable.call(value) : value;
    }
}
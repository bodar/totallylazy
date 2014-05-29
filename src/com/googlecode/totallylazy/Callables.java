package com.googlecode.totallylazy;

import com.googlecode.totallylazy.comparators.Comparators;

import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.util.Comparator;
import java.util.Iterator;
import java.util.concurrent.Callable;

import static com.googlecode.totallylazy.Methods.method;
import static com.googlecode.totallylazy.Sequences.sequence;

public interface Callables {
    static <T> Function<Value<T>, T> value() {
        return Value<T>::value;
    }

    static <T> Function<Value<T>, T> value(Class<T> aClass) {
        return value();
    }

    static <T, R> Function<T, R> asFunction(final Callable<? extends R> callable) {
        return t -> callable.call();
    }

    static <T> Unary<T> nullGuard(final Function<? super T, ? extends T> callable) {
        return o -> {
            if (o == null) return null;
            return callable.call(o);
        };
    }

    static <T> Unary<Sequence<T>> reduceAndShift(final Function2<? super T, ? super T, ? extends T> action) {
        return values -> values.tail().append(values.reduceLeft(action));
    }

    static <T, S> Function<T, S> cast(final Class<? extends S> aClass) {
        return aClass::cast;
    }

    static <T, S> Function<T, S> cast() {
        return Unchecked::cast;
    }

    static Function<Object, Class<?>> toClass() {
        return o -> {
            if (o == null) return Void.class;
            return o.getClass();
        };
    }

    static <T, R extends Comparable<? super R>> Comparator<T> ascending(final Function<? super T, ? extends R> callable) {
        return Comparators.ascending(callable);
    }

    static <T, R extends Comparable<? super R>> Comparator<T> descending(final Function<? super T, ? extends R> callable) {
        return Comparators.descending(callable);
    }

    static Function<Object, Integer> size() {
        return length;
    }

    static Function<Object, Integer> length() {
        return length;
    }

    static final Function<Object, Integer> length = instance -> {
            Class aClass = instance.getClass();
            if (aClass.isArray()) {
                return Array.getLength(instance);
            }
            return sequence(method(instance, "size"), method(instance, "length")).
                    flatMap(Option.identity(Method.class)).
                    headOption().
                    map(Methods.<Integer>invokeOn(instance)).
                    getOrElse(Callables.<Integer>callThrows(new UnsupportedOperationException("Does not support fields yet")));
        };

    static <T> Unary<Sequence<T>> realise() {
        return Sequence<T>::realise;
    }

    static <T> Function<First<T>, T> first(Class<T> aClass) {
        return first();
    }

    static <T> Function<First<T>, T> first() {
        return First<T>::first;
    }

    static <T> Function<Iterable<T>, T> last(Class<T> t) {
        return last();
    }

    static <T> Function<Iterable<T>, T> last() {
        return Sequences::last;
    }

    static <F, S, R> Function<Pair<F, S>, Pair<R, S>> first(final Function<? super F, ? extends R> firstCallable, Class<S> sClass) {
        return first(firstCallable);
    }

    static <F, S, R> Function<Pair<F, S>, Pair<R, S>> first(final Function<? super F, ? extends R> firstCallable) {
        return pair -> Pair.pair(firstCallable.call(pair.first()), pair.second());
    }

    static <T> Function<Second<T>, T> second(Class<T> aClass) {
        return second();
    }

    static <T> Function<Second<T>, T> second() {
        return Second<T>::second;
    }

    static <F, S, R> Function<Pair<F, S>, Pair<F, R>> second(final Function<? super S, ? extends R> secondCallable) {
        return pair -> Pair.pair(pair.first(), secondCallable.call(pair.second()));
    }

    static <T> Function<Third<T>, T> third(Class<T> aClass) {
        return third();
    }

    static <T> Function<Third<T>, T> third() {
        return Third<T>::third;
    }

    static <F, S, T, R> Function<Triple<F, S, T>, Triple<F, S, R>> third(final Function<? super T, ? extends R> thirdCallable) {
        return triple -> Triple.triple(triple.first(), triple.second(), thirdCallable.call(triple.third()));
    }

    static <T> Function<Fourth<T>, T> fourth(Class<T> aClass) {
        return fourth();
    }

    static <T> Function<Fourth<T>, T> fourth() {
        return Fourth<T>::fourth;
    }

    static <F, S, T, Fo, R> Function<Quadruple<F, S, T, Fo>, Quadruple<F, S, T, R>> fourth(final Function<? super Fo, ? extends R> fourthCallable) {
        return quadruple -> Quadruple.quadruple(quadruple.first(), quadruple.second(), quadruple.third(), fourthCallable.call(quadruple.fourth()));
    }

    static <T> Function<Fifth<T>, T> fifth(Class<T> aClass) {
        return fifth();
    }

    static <T> Function<Fifth<T>, T> fifth() {
        return Fifth<T>::fifth;
    }

    static <F, S, T, Fo, Fi, R> Function<Quintuple<F, S, T, Fo, Fi>, Quintuple<F, S, T, Fo, R>> fifth(final Function<? super Fi, ? extends R> callable) {
        return quintuple -> Quintuple.quintuple(quintuple.first(), quintuple.second(), quintuple.third(), quintuple.fourth(), callable.call(quintuple.fifth()));
    }

    static <T> Function<Iterable<T>, T> head() {
        return Sequences::head;
    }

    static final Function<Object, String> toString = new Function<Object, String>() {
        public final String call(final Object value) {
            return value.toString();
        }

        @Override
        public String toString() {
            return "toString";
        }
    };

    static Function<Object, String> asString() {
        return toString;
    }

    static final Function2<Integer, Object, Integer> hashCode = (hash, value) -> {
        if (value == null) return hash * 19;
        int current = value.hashCode();
        return (current == 0 ? 19 : current) * hash;
    };

    static Function2<Integer, Object, Integer> asHashCode() {
        return hashCode;
    }


    static <T> Function<Iterable<T>, Iterator<T>> asIterator() {
        return Iterable<T>::iterator;
    }

    static <T> Function<Iterator<T>, Iterable<T>> asIterable() {
        return Sequences::forwardOnly;
    }

    static <T> Returns<T> returns(final T t) {
        return Functions.returns(t);
    }

    static <T, R> Function<T, R> ignoreAndReturn(final R r) {
        return returns1(r);
    }

    static <A, B> Function<A, B> returns1(B result) {
        return Functions.returns1(result);
    }

    static <A, B, C> Function2<A, B, C> returns2(C result) {
        return Functions.returns2(result);
    }

    static <T> Unary<T> returnArgument() {
        return Functions.identity();
    }

    static <T> Unary<T> returnArgument(final Class<T> aClass) {
        return returnArgument();
    }

    static <T> Returns<T> aNull(final Class<T> aClass) {
        return () -> null;
    }

    static <T> Returns<T> callThrows(final Exception e) {
        return () -> {
            throw e;
        };
    }

    static <T> Returns<T> callThrows(final Exception e, final Class<T> aClass) {
        return callThrows(e);
    }

    static <T> Function<Callable<T>, T> call() {
        return Callable<T>::call;
    }

    static <T> Function<Callable<T>, T> call(final Class<T> aClass) {
        return call();
    }

    static <T, R> Function<Function<T, R>, R> callWith(final T value) {
        return callable -> callable.call(value);
    }

    static <A, B, C> Returns<C> deferApply(final Function2<? super A, ? super B, ? extends C> callable, final A a, final B b) {
        return () -> callable.call(a, b);
    }

    static <A, B> Returns<B> deferApply(final Function<? super A, ? extends B> callable, final A value) {
        return () -> callable.call(value);
    }

    static <A, B> Function<A, Returns<B>> deferReturn(final Function<? super A, ? extends B> callable) {
        return a -> deferApply(callable, a);
    }

    static <A, B, C> Function<B, C> apply(final Function2<? super A, ? super B, ? extends C> callable, final A value) {
        return b -> callable.call(value, b);
    }

    static <A, B, C, D> Function2<B, C, D> apply(final Function3<? super A, ? super B, ? super C, ? extends D> callable, final A value) {
        return (b, c) -> callable.call(value, b, c);
    }

    static <A, B, C> Function<A, Function<B, C>> curry(final Function2<? super A, ? super B, ? extends C> callable) {
        return Unchecked.cast(callable);
    }

    static <A, B, C, D> Function<A, Function<B, Function<C, D>>> curry(final Function3<? super A, ? super B, ? super C, ? extends D> callable) {
        return Unchecked.cast(callable);
    }

    static <A, B, C> Function2<A, B, C> uncurry2(final Function<? super A, ? extends Function<? super B, ? extends C>> callable) {
        return Functions.uncurry2(callable);
    }

    static <A, B, C, D> Function3<A, B, C, D> uncurry3(final Function<? super A, ? extends Function<? super B, ? extends Function<? super C, ? extends D>>> callable) {
        return Functions.uncurry3(callable);
    }

    static <L> Function<Either<L, ?>, L> left(Class<L> aClass) {
        return left();
    }

    static <L> Function<Either<L, ?>, L> left() {
        return either -> either.left();
    }

    static <R> Function<Either<?, R>, R> right(Class<R> aClass) {
        return right();
    }

    static <R> Function<Either<?, R>, R> right() {
        return either -> either.right();
    }

    static <A, B, C> Function2<B, A, C> flip(final Function2<? super A, ? super B, ? extends C> callable) {
        return (s, t) -> callable.call(t, s);
    }

    static <A, B, C> Function<A, C> compose(final Function<? super A, ? extends B> first, final Function<? super B, ? extends C> second) {
        return a -> second.call(first.call(a));
    }

    static <A, B> Function<A, B> compose(final Function<? super A, ?> ignoreResult, final Callable<? extends B> callable) {
        return a -> {
            ignoreResult.call(a);
            return callable.call();
        };
    }

    static <A, B> Function<A, B> doThen(final Function<? super A, ?> ignoreResult, final Callable<? extends B> callable) {
        return compose(ignoreResult, callable);
    }

    static <A, B> Returns<B> compose(final Callable<? extends A> first, final Function<? super A, ? extends B> second) {
        return () -> second.call(first.call());
    }


    static <A, B> Function<A, B> interruptable(final Function<? super A, ? extends B> function) {
        return Functions.interruptable(function);
    }

    static <A, B, C> Function<Pair<A, B>, C> pair(final Function2<? super A, ? super B, ? extends C> function) {
        return Functions.pair(function);
    }

    static <A, B, C> Function2<A, B, C> unpair(final Function<? super Pair<? extends A, ? extends B>, ? extends C> function) {
        return Functions.unpair(function);
    }

    static <A, B, C, D> Function<Triple<A, B, C>, D> triple(final Function3<? super A, ? super B, ? super C, ? extends D> callable) {
        return Functions.triple(callable);
    }

    static <A, B, C, D> Function3<A, B, C, D> untriple(final Function<? super Triple<? extends A, ? extends B, ? extends C>, ? extends D> callable) {
        return Functions.untriple(callable);
    }

    static <L, R> Function<L, Either<L, R>> asLeft() {
        return Either.functions.asLeft();
    }

    static <L, R> Function<R, Either<L, R>> asRight() {
        return Either.functions.asRight();
    }

    static <T> Unary<T> replace(final Predicate<? super T> predicate, final Function<? super T, ? extends T> callable) {
        return when(predicate, callable);
    }

    static <T> Unary<T> when(final Predicate<? super T> predicate, final Function<? super T, ? extends T> callable) {
        return value -> predicate.matches(value) ? callable.call(value) : value;
    }
}
package com.googlecode.totallylazy;

import com.googlecode.totallylazy.comparators.Comparators;

import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.util.Comparator;
import java.util.Iterator;
import java.util.concurrent.Callable;

import static com.googlecode.totallylazy.Methods.method;
import static com.googlecode.totallylazy.Sequences.sequence;

public final class Callables {
    public static <T> Function<Value<T>, T> value() {
        return Value<T>::value;
    }

    public static <T> Function<Value<T>, T> value(Class<T> aClass) {
        return value();
    }

    public static <T, R> Function<T, R> asCallable1(final Callable<? extends R> callable) {
        return t -> callable.call();
    }

    public static <T> UnaryOperator<T> nullGuard(final Function<? super T, ? extends T> callable) {
        return o -> {
            if (o == null) return null;
            return callable.call(o);
        };
    }

    public static <T> UnaryOperator<Sequence<T>> reduceAndShift(final Callable2<? super T, ? super T, ? extends T> action) {
        return values -> values.tail().append(values.reduceLeft(action));
    }

    public static <T, S> Function<T, S> cast(final Class<? extends S> aClass) {
        return aClass::cast;
    }

    public static <T, S> Function<T, S> cast() {
        return Unchecked::cast;
    }

    public static Function<Object, Class<?>> toClass() {
        return o -> {
            if (o == null) return Void.class;
            return o.getClass();
        };
    }

    public static <T, R extends Comparable<? super R>> Comparator<T> ascending(final Function<? super T, ? extends R> callable) {
        return Comparators.ascending(callable);
    }

    public static <T, R extends Comparable<? super R>> Comparator<T> descending(final Function<? super T, ? extends R> callable) {
        return Comparators.descending(callable);
    }

    public static Function<Object, Integer> size() {
        return length();
    }

    public static Function<Object, Integer> length() {
        return instance -> {
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
    }


    public static <T> UnaryOperator<Sequence<T>> realise() {
        return Sequence<T>::realise;
    }

    public static <T> Function<First<T>, T> first(Class<T> aClass) {
        return first();
    }

    public static <T> Function<First<T>, T> first() {
        return First<T>::first;
    }

    public static <T> Function<Iterable<T>, T> last(Class<T> t) {
        return last();
    }

    public static <T> Function<Iterable<T>, T> last() {
        return Sequences::last;
    }

    public static <F, S, R> Function<Pair<F, S>, Pair<R, S>> first(final Function<? super F, ? extends R> firstCallable, Class<S> sClass) {
        return first(firstCallable);
    }

    public static <F, S, R> Function<Pair<F, S>, Pair<R, S>> first(final Function<? super F, ? extends R> firstCallable) {
        return p -> Pair.pair(firstCallable.call(p.first()), p.second());
    }

    public static <T> Function<Second<T>, T> second(Class<T> aClass) {
        return second();
    }

    public static <T> Function<Second<T>, T> second() {
        return Second<T>::second;
    }

    public static <F, S, R> Function<Pair<F, S>, Pair<F, R>> second(final Function<? super S, ? extends R> secondCallable) {
        return p -> Pair.pair(p.first(), secondCallable.call(p.second()));
    }

    public static <T> Function<Third<T>, T> third(Class<T> aClass) {
        return third();
    }

    public static <T> Function<Third<T>, T> third() {
        return Third<T>::third;
    }

    public static <F, S, T, R> Function<Triple<F, S, T>, Triple<F, S, R>> third(final Function<? super T, ? extends R> thirdCallable) {
        return t -> Triple.triple(t.first(), t.second(), thirdCallable.call(t.third()));
    }

    public static <T> Function<Fourth<T>, T> fourth(Class<T> aClass) {
        return fourth();
    }

    public static <T> Function<Fourth<T>, T> fourth() {
        return Fourth<T>::fourth;
    }

    public static <F, S, T, Fo, R> Function<Quadruple<F, S, T, Fo>, Quadruple<F, S, T, R>> fourth(final Function<? super Fo, ? extends R> fourthCallable) {
        return q -> Quadruple.quadruple(q.first(), q.second(), q.third(), fourthCallable.call(q.fourth()));
    }

    public static <T> Function<Fifth<T>, T> fifth(Class<T> aClass) {
        return fifth();
    }

    public static <T> Function<Fifth<T>, T> fifth() {
        return Fifth<T>::fifth;
    }

    public static <F, S, T, Fo, Fi, R> Function<Quintuple<F, S, T, Fo, Fi>, Quintuple<F, S, T, Fo, R>> fifth(final Function<? super Fi, ? extends R> callable) {
        return q -> Quintuple.quintuple(q.first(), q.second(), q.third(), q.fourth(), callable.call(q.fifth()));
    }

    public static <T> Function<Iterable<T>, T> head() {
        return Sequences::head;
    }

    public final static Function<Object, String> toString = new Function<Object, String>() {
        public final String call(final Object value) {
            return value.toString();
        }

        @Override
        public String toString() {
            return "toString";
        }
    };

    public static <T> Function<Object, String> asString() {
        return toString;
    }

    public static final Function2<Integer, Object, Integer> hashCode = new Function2<Integer, Object, Integer>() {
        public Integer call(Integer hash, Object value) throws Exception {
            if (value == null) return hash * 19;
            int current = value.hashCode();
            return (current == 0 ? 19 : current) * hash;
        }
    };

    public static Function2<Integer, Object, Integer> asHashCode() {
        return hashCode;
    }


    public static <T> Function<Iterable<T>, Iterator<T>> asIterator() {
        return Iterable<T>::iterator;
    }

    public static <T> Function<Iterator<T>, Iterable<T>> asIterable() {
        return Sequences::forwardOnly;
    }

    public static <T> Returns<T> returns(final T t) {
        return Functions.returns(t);
    }

    public static <T, R> Function<T, R> ignoreAndReturn(final R r) {
        return returns1(r);
    }

    public static <A, B> Function<A, B> returns1(B result) {
        return Functions.returns1(result);
    }

    public static <A, B, C> Function2<A, B, C> returns2(C result) {
        return Functions.returns2(result);
    }

    public static <T> UnaryOperator<T> returnArgument() {
        return Functions.identity();
    }

    public static <T> UnaryOperator<T> returnArgument(final Class<T> aClass) {
        return returnArgument();
    }

    public static <T> Returns<T> aNull(final Class<T> aClass) {
        return () -> null;
    }

    public static <T> Returns<T> callThrows(final Exception e) {
        return () -> {
            throw e;
        };
    }

    public static <T> Returns<T> callThrows(final Exception e, final Class<T> aClass) {
        return callThrows(e);
    }

    public static <T> Function<Callable<T>, T> call() {
        return callable -> callable.call();
    }

    public static <T> Function<Callable<T>, T> call(final Class<T> aClass) {
        return call();
    }

    public static <T, R> Function<Function<T, R>, R> callWith(final T value) {
        return callable -> callable.call(value);
    }

    public static <A, B, C> Returns<C> deferApply(final Callable2<? super A, ? super B, ? extends C> callable, final A a, final B b) {
        return () -> callable.call(a, b);
    }

    public static <A, B> Returns<B> deferApply(final Function<? super A, ? extends B> callable, final A value) {
        return () -> callable.call(value);
    }

    public static <A, B> Function<A, Returns<B>> deferReturn(final Function<? super A, ? extends B> callable) {
        return a -> deferApply(callable, a);
    }

    public static <A, B, C> Function<B, C> apply(final Callable2<? super A, ? super B, ? extends C> callable, final A value) {
        return Functions.apply(callable, value);
    }

    public static <A, B, C, D> Function2<B, C, D> apply(final Callable3<? super A, ? super B, ? super C, ? extends D> callable, final A value) {
        return Functions.apply(callable, value);
    }

    public static <A, B, C> Function<A, Function<B, C>> curry(final Callable2<? super A, ? super B, ? extends C> callable) {
        return (a) -> (b) -> callable.call(a, b);
    }

    public static <A, B, C, D> Function<A, Function<B, Function<C, D>>> curry(final Callable3<? super A, ? super B, ? super C, ? extends D> callable) {
        return (a) -> (b) -> (c) -> callable.call(a, b, c);
    }

    public static <A, B, C> Function2<A, B, C> uncurry2(final Function<? super A, ? extends Function<? super B, ? extends C>> callable) {
        return Functions.uncurry2(callable);
    }

    public static <A, B, C, D> Function3<A, B, C, D> uncurry3(final Function<? super A, ? extends Function<? super B, ? extends Function<? super C, ? extends D>>> callable) {
        return Functions.uncurry3(callable);
    }

    public static <L> Function<Either<L, ?>, L> left(Class<L> aClass) {
        return left();
    }

    public static <L> Function<Either<L, ?>, L> left() {
        return either -> either.left();
    }

    public static <R> Function<Either<?, R>, R> right(Class<R> aClass) {
        return right();
    }

    public static <R> Function<Either<?, R>, R> right() {
        return either -> either.right();
    }

    public static <A, B, C> Function2<B, A, C> flip(final Callable2<? super A, ? super B, ? extends C> callable) {
        return new Function2<B, A, C>() {
            public C call(B s, A t) throws Exception {
                return callable.call(t, s);
            }
        };
    }

    public static <A, B, C> Function<A, C> compose(final Function<? super A, ? extends B> first, final Function<? super B, ? extends C> second) {
        return a -> second.call(first.call(a));
    }

    public static <A, B> Function<A, B> compose(final Function<? super A, ?> ignoreResult, final Callable<? extends B> callable) {
        return a -> {
            ignoreResult.call(a);
            return callable.call();
        };
    }

    public static <A, B> Function<A, B> doThen(final Function<? super A, ?> ignoreResult, final Callable<? extends B> callable) {
        return compose(ignoreResult, callable);
    }

    public static <A, B> Returns<B> compose(final Callable<? extends A> first, final Function<? super A, ? extends B> second) {
        return () -> second.call(first.call());
    }


    public static <A, B> Function<A, B> interruptable(final Function<? super A, ? extends B> function) {
        return Functions.interruptable(function);
    }

    public static <A, B, C> Function<Pair<A, B>, C> pair(final Callable2<? super A, ? super B, ? extends C> function) {
        return Functions.pair(function);
    }

    public static <A, B, C> Function2<A, B, C> unpair(final Function<? super Pair<? extends A, ? extends B>, ? extends C> function) {
        return Functions.unpair(function);
    }

    public static <A, B, C, D> Function<Triple<A, B, C>, D> triple(final Callable3<? super A, ? super B, ? super C, ? extends D> callable) {
        return Functions.triple(callable);
    }

    public static <A, B, C, D> Function3<A, B, C, D> untriple(final Function<? super Triple<? extends A, ? extends B, ? extends C>, ? extends D> callable) {
        return Functions.untriple(callable);
    }

    public static <L, R> Function<L, Either<L, R>> asLeft() {
        return Either.functions.asLeft();
    }

    public static <L, R> Function<R, Either<L, R>> asRight() {
        return Either.functions.asRight();
    }

    public static <T> UnaryOperator<T> replace(final Predicate<? super T> predicate, final Function<? super T, ? extends T> callable) {
        return when(predicate, callable);
    }

    public static <T> UnaryOperator<T> when(final Predicate<? super T> predicate, final Function<? super T, ? extends T> callable) {
        return value -> predicate.matches(value) ? callable.call(value) : value;
    }
}
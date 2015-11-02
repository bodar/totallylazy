package com.googlecode.totallylazy;

import com.googlecode.totallylazy.functions.*;

import java.util.Map;
import java.util.concurrent.Callable;

import static com.googlecode.totallylazy.Callers.call;
import static com.googlecode.totallylazy.functions.Functions.returns;
import static com.googlecode.totallylazy.Sequences.sequence;

public class Pair<F, S> implements First<F>, Second<S>, Value<F>, Functor<F>, Map.Entry<F,S> {
    private final Lazy<F> first;
    private final Lazy<S> second;

    protected Pair(final Callable<? extends F> first, final Callable<? extends S> second) {
        this.first = Lazy.lazy(first);
        this.second = Lazy.lazy(second);
    }

    public static <F, S> Pair<F, S> pair(final F first, final S second) {
        return new Pair<F, S>(returns(first), returns(second));
    }

    public static <F, S> Pair<F, S> pair(final Callable<? extends F> first, final Callable<? extends S> second) {
        return new Pair<F, S>(first, second);
    }

    public static <F, S> Curried2<F, S, Pair<F, S>> pair() {
        return Pair::pair;
    }

    public final F first() {
        return first.value();
    }

    public final S second() {
        return second.value();
    }

    @Override
    public F value() {
        return first();
    }

    @Override
    public String toString() {
        return toString("[", ",", "]");
    }

    public String toString(String separator) {
        return toString("", separator, "");
    }

    public String toString(String start, String separator, String end) {
        return values().toString(start, separator, end);
    }

    public Sequence<Object> values() {
        return sequence(first(), second());
    }

    @Override
    public F getKey() {
        return first();
    }

    @Override
    public S getValue() {
        return second();
    }

    @Override
    public S setValue(S value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean equals(final Object o) {
        return o instanceof Pair && values().equals(((Pair) o).values());
    }

    @Override
    public int hashCode() {
        return values().hashCode();
    }

    public static <A, B, C> Curried2<Pair<A, B>, C, Pair<B, C>> leftShift2() {
        return Pair::leftShift;
    }

    public static <A, B, C> Pair<B, C> leftShift(Pair<? extends A, ? extends B> pair, C c) {
        return Pair.pair(pair.second(), c);
    }

    public static <A, B, C> Pair<B, C> reduceLeftShift(final Pair<? extends A, ? extends B> pair, final Function2<? super A, ? super B, ? extends C> callable) {
        return Pair.leftShift(pair, call(callable, pair.first(), pair.second()));
    }

    public static <A, B, C> Curried2<Pair<A, B>, Function2<A, B, C>, Pair<B, C>> reduceLeftShift() {
        return Pair::reduceLeftShift;
    }

    public static <A, B, C> Function1<Pair<A, B>, Pair<B, C>> reduceLeftShift(Function2<A, B, C> callable) {
        return Pair.<A, B, C>reduceLeftShift().flip().apply(callable);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <NewF> Pair<NewF, S> map(final Function1<? super F, ? extends NewF> callable) {
        return pair(first.map(callable), second);
    }

    public <R> Pair<R, S> first(Function1<? super F, ? extends R> map) {
        return pair(first.then(map), second);
    }

    public <R> Pair<F,R> second(Function1<? super S, ? extends R> map) {
        return pair(first, second.then(map));
    }

    public static class functions {
        public static <T> Function1<First<T>, T> first() {
            return Callables.first();
        }

        public static <T> Function1<First<T>, T> first(Class<T> aClass) {
            return first();
        }

        public static <T> Function1<Second<T>, T> second() {
            return Callables.second();
        }

        public static <T> Function1<Second<T>, T> second(Class<T> aClass) {
            return second();
        }

        public static <F1, F2, S> Function1<Pair<F1, S>, Pair<F2, S>> replaceFirst(final F2 newFirst) {
            return pair -> pair(newFirst, pair.second());
        }
        public static <F1, F2, S> Function1<Pair<F1, S>, Pair<F2, S>> replaceFirst(final F2 newFirst, final Class<S> aClass) {
            return replaceFirst(newFirst);
        }

        public static <F1, F2, S> Function1<Pair<F1, S>, Pair<F2, S>> replaceFirst(final Function1<F1,F2> map) {
            return pair -> pair(map.call(pair.first()), pair.second());
        }

        public static <F1, F2, S> Function1<Pair<F1, S>, Pair<F2, S>> replaceFirst(final Function1<F1,F2> map, Class<S> aClass) {
            return replaceFirst(map);
        }

        public static <F, S1, S2> Function1<Pair<F, S1>, Pair<F, S2>> replaceSecond(final S2 newSecond) {
            return pair -> pair(pair.first(), newSecond);
        }

        public static <F, S1, S2> Function1<Pair<F, S1>, Pair<F, S2>> replaceSecond(final Class<F> aClass, final S2 map) {
            return replaceSecond(map);
        }

        public static <F, S1, S2> Function1<Pair<F, S1>, Pair<F, S2>> replaceSecond(final Function1<S1,S2> map) {
            return pair -> pair(pair.first(), map.call(pair.second()));
        }

        public static <F, S1, S2> Function1<Pair<F, S1>, Pair<F, S2>> replaceSecond(Class<F> aClass, final Function1<S1,S2> newSecond) {
            return replaceSecond(newSecond);
        }

        public static <F, S> Function1<S, Pair<F, S>> toPairWithFirst(final F first) {
            return second1 -> pair(first, second1);
        }

        public static <F, S> Function1<F, Pair<F, S>> toPairWithSecond(final S second) {
            return first1 -> pair(first1, second);
        }

        public static Function1<Pair<?,?>, Sequence<Object>> values() {
            return Pair::values;
        }

        public static Function1<Pair<?,?>, String> toString(final String separator) {
            return pair -> pair.toString(separator);
        }

        public static Function1<Pair<?,?>, String> toString(final String start, final String separator, final String end) {
            return pair -> pair.toString(start, separator, end);
        }

        public static Function1<Pair<?,?>, String> pairToString(final String separator) {
            return pair -> pair.toString(separator);
        }

        public static Function1<Pair<?,?>, String> pairToString(final String start, final String separator, final String end) {
            return pair -> pair.toString(start, separator, end);
        }
    }
}

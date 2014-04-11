package com.googlecode.totallylazy;

import java.util.Map;
import java.util.concurrent.Callable;

import static com.googlecode.totallylazy.Callers.call;
import static com.googlecode.totallylazy.Sequences.sequence;

public interface Pair<F, S> extends First<F>, Second<S>, Value<F>, Functor<F>, Map.Entry<F, S>, Tuple {
    static abstract class AbstractPair<F,S> extends AbstractTuple implements Pair<F,S>{}

    static <F, S> Pair<F, S> pair(final F first, final S second) {
        return new AbstractPair<F, S>() {
            @Override public F first() { return first; }
            @Override public S second() { return second; }
        };
    }

    static <F, S> Pair<F, S> pair(final Callable<? extends F> first, final Callable<? extends S> second) {
        return new AbstractPair<F, S>() {
            @Override public F first() { return Functions.call(first); }
            @Override public S second() { return Functions.call(second); }
        };
    }


    @Override
    default F value() {
        return first();
    }

    @Override
    default F getKey() {
        return first();
    }

    @Override
    default S getValue() {
        return second();
    }

    @Override
    default S setValue(S value) {
        throw new UnsupportedOperationException();
    }

    default Seq<Object> values() {
        return sequence(first(), second());
    }


    static <A, B, C> Pair<B, C> leftShift(Pair<? extends A, ? extends B> pair, C c) {
        return Pair.pair(pair.second(), c);
    }

    static <A, B, C> Pair<B, C> reduceLeftShift(final Pair<? extends A, ? extends B> pair, final Function2<? super A, ? super B, ? extends C> callable) {
        return Pair.leftShift(pair, call(callable, pair.first(), pair.second()));
    }

    static <A, B, C> Function2<Pair<A, B>, Function2<A, B, C>, Pair<B, C>> reduceLeftShift() {
        return new Function2<Pair<A, B>, Function2<A, B, C>, Pair<B, C>>() {
            @Override
            public Pair<B, C> call(Pair<A, B> pair, Function2<A, B, C> callable) throws Exception {
                return reduceLeftShift(pair, callable);
            }
        };
    }

    public static <A, B, C> Function<Pair<A, B>, Pair<B, C>> reduceLeftShift(Function2<A, B, C> callable) {
        return Pair.<A, B, C>reduceLeftShift().flip().apply(callable);
    }

    @Override
    @SuppressWarnings("unchecked")
    default <NewF> Pair<NewF, S> map(final Function<? super F, ? extends NewF> callable) {
        return pair(callable.apply(first()), second());
    }

    default <R> Pair<R, S> first(Function<? super F, ? extends R> map) {
        return pair(map.apply(first()), second());
    }

    default <R> Pair<F, R> second(Function<? super S, ? extends R> map) {
        return pair(first(), map.apply(second()));
    }

    static class functions {
        public static <T> Function<First<T>, T> first() {
            return Callables.first();
        }

        public static <T> Function<First<T>, T> first(Class<T> aClass) {
            return first();
        }

        public static <T> Function<Second<T>, T> second() {
            return Callables.second();
        }

        public static <T> Function<Second<T>, T> second(Class<T> aClass) {
            return second();
        }

        public static <F1, F2, S> Function<Pair<F1, S>, Pair<F2, S>> replaceFirst(final F2 newFirst) {
            return pair -> pair(newFirst, pair.second());
        }

        public static <F1, F2, S> Function<Pair<F1, S>, Pair<F2, S>> replaceFirst(final F2 newFirst, final Class<S> aClass) {
            return replaceFirst(newFirst);
        }

        public static <F1, F2, S> Function<Pair<F1, S>, Pair<F2, S>> replaceFirst(final Function<F1, F2> map) {
            return pair -> pair(map.call(pair.first()), pair.second());
        }

        public static <F1, F2, S> Function<Pair<F1, S>, Pair<F2, S>> replaceFirst(final Function<F1, F2> map, Class<S> aClass) {
            return replaceFirst(map);
        }

        public static <F, S1, S2> Function<Pair<F, S1>, Pair<F, S2>> replaceSecond(final S2 newSecond) {
            return pair -> pair(pair.first(), newSecond);
        }

        public static <F, S1, S2> Function<Pair<F, S1>, Pair<F, S2>> replaceSecond(final Class<F> aClass, final S2 map) {
            return replaceSecond(map);
        }

        public static <F, S1, S2> Function<Pair<F, S1>, Pair<F, S2>> replaceSecond(final Function<S1, S2> map) {
            return pair -> pair(pair.first(), map.call(pair.second()));
        }

        public static <F, S1, S2> Function<Pair<F, S1>, Pair<F, S2>> replaceSecond(Class<F> aClass, final Function<S1, S2> newSecond) {
            return replaceSecond(newSecond);
        }

        public static <F, S> Function<S, Pair<F, S>> toPairWithFirst(final F first) {
            return second -> pair(first, second);
        }

        public static <F, S> Function<F, Pair<F, S>> toPairWithSecond(final S second) {
            return first -> pair(first, second);
        }

        public static Function<Pair<?, ?>, Seq<Object>> values() {
            return Pair::values;
        }

        public static Function<Pair<?, ?>, String> toString(final String separator) {
            return pair -> pair.toString(separator);
        }

        public static Function<Pair<?, ?>, String> toString(final String start, final String separator, final String end) {
            return pair -> pair.toString(start, separator, end);
        }

        public static Function<Pair<?, ?>, String> pairToString(final String separator) {
            return pair -> pair.toString(separator);
        }

        public static Function<Pair<?, ?>, String> pairToString(final String start, final String separator, final String end) {
            return pair -> pair.toString(start, separator, end);
        }

        static <A, B, C> Function2<Pair<A, B>, C, Pair<B, C>> leftShift() {
            return Pair::leftShift;
        }

    }
}

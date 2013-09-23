package com.googlecode.totallylazy;

import java.util.Map;
import java.util.concurrent.Callable;

import static com.googlecode.totallylazy.Callers.call;
import static com.googlecode.totallylazy.Functions.returns;
import static com.googlecode.totallylazy.Sequences.sequence;
import static com.googlecode.totallylazy.callables.LazyCallable.lazy;

public class Pair<F, S> implements First<F>, Second<S>, Value<F>, Functor<F>, Map.Entry<F,S> {
    private final Lazy<F> first;
    private final Lazy<S> second;

    protected Pair(final Callable<? extends F> first, final Callable<? extends S> second) {
        this.first = lazy(first);
        this.second = lazy(second);
    }

    public static <F, S> Pair<F, S> pair(final F first, final S second) {
        return new Pair<F, S>(returns(first), returns(second));
    }

    public static <F, S> Pair<F, S> pair(final Callable<? extends F> first, final Callable<? extends S> second) {
        return new Pair<F, S>(first, second);
    }

    public static <F, S> Function2<F, S, Pair<F, S>> pair() {
        return new Function2<F, S, Pair<F, S>>() {
            @Override
            public Pair<F, S> call(F f, S s) throws Exception {
                return Pair.pair(f, s);
            }
        };
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
    public final String toString() {
        return toString("[", ",", "]");
    }

    public final String toString(String separator) {
        return toString("", separator, "");
    }

    public final String toString(String start, String separator, String end) {
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
    public final boolean equals(final Object o) {
        return o instanceof Pair && values().equals(((Pair) o).values());
    }

    @Override
    public final int hashCode() {
        return values().hashCode();
    }

    public static <A, B, C> Function2<Pair<A, B>, C, Pair<B, C>> leftShift() {
        return new Function2<Pair<A, B>, C, Pair<B, C>>() {
            @Override
            public Pair<B, C> call(Pair<A, B> pair, C c) throws Exception {
                return leftShift(pair, c);
            }
        };
    }

    public static <A, B, C> Pair<B, C> leftShift(Pair<? extends A, ? extends B> pair, C c) {
        return Pair.pair(pair.second(), c);
    }

    public static <A, B, C> Pair<B, C> reduceLeftShift(final Pair<? extends A, ? extends B> pair, final Callable2<? super A, ? super B, ? extends C> callable) {
        return Pair.leftShift(pair, call(callable, pair.first(), pair.second()));
    }

    public static <A, B, C> Function2<Pair<A, B>, Callable2<A, B, C>, Pair<B, C>> reduceLeftShift() {
        return new Function2<Pair<A, B>, Callable2<A, B, C>, Pair<B, C>>() {
            @Override
            public Pair<B, C> call(Pair<A, B> pair, Callable2<A, B, C> callable) throws Exception {
                return reduceLeftShift(pair, callable);
            }
        };
    }

    /** @deprecated Replaced by {@link Pair.functions#toString(String)}  } */
    @Deprecated
    public static Function1<Pair, String> asString(final String seperator) {
        return new Function1<Pair, String>() {
            @Override
            public String call(Pair pair) throws Exception {
                return pair.toString(seperator);
            }
        };
    }

    public static <A, B, C> Function1<Pair<A, B>, Pair<B, C>> reduceLeftShift(Callable2<A, B, C> callable) {
        return Pair.<A, B, C>reduceLeftShift().flip().apply(callable);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <NewF> Pair<NewF, S> map(final Callable1<? super F, ? extends NewF> callable) {
        return pair(first.map(callable), second);
    }

    public <R> Pair<R, S> first(Callable1<? super F, ? extends R> map) {
        return pair(first.then(map), second);
    }

    public <R> Pair<F,R> second(Callable1<? super S, ? extends R> map) {
        return pair(first, second.then(map));
    }

    public static class functions {
        public static <F, S1, S2> Function1<Pair<F, S1>, Pair<F, S2>> replaceSecond(final S2 newSecond) {
            return new Function1<Pair<F, S1>, Pair<F, S2>>() {
                @Override
                public Pair<F, S2> call(Pair<F, S1> pair) throws Exception {
                    return pair(pair.first(), newSecond);
                }
            };
        }

        public static <F1, F2, S> Function1<Pair<F1, S>, Pair<F2, S>> replaceFirst(final F2 newFirst) {
            return new Function1<Pair<F1, S>, Pair<F2, S>>() {
                @Override
                public Pair<F2, S> call(Pair<F1, S> pair) throws Exception {
                    return pair(newFirst, pair.second());
                }
            };
        }

        public static <F, S> Function1<S, Pair<F, S>> toPairWithFirst(final F first) {
            return new Function1<S, Pair<F, S>>() {
                @Override
                public Pair<F, S> call(S second) throws Exception {
                    return pair(first, second);
                }
            };
        }

        public static <F, S> Function1<F, Pair<F, S>> toPairWithSecond(final S second) {
            return new Function1<F, Pair<F, S>>() {
                @Override
                public Pair<F, S> call(F first) throws Exception {
                    return pair(first, second);
                }
            };
        }

        public static Mapper<Pair<?,?>, Sequence<Object>> values() {
            return new Mapper<Pair<?,?>, Sequence<Object>>() {
                @Override
                public Sequence<Object> call(Pair<?,?> pair) throws Exception {
                    return pair.values();
                }
            };
        }

        public static Mapper<Pair<?,?>, String> toString(final String separator) {
            return new Mapper<Pair<?,?>, String>() {
                @Override
                public String call(Pair<?,?> pair) throws Exception {
                    return pair.toString(separator);
                }
            };
        }

        public static Mapper<Pair<?,?>, String> toString(final String start, final String separator, final String end) {
            return new Mapper<Pair<?,?>, String>() {
                @Override
                public String call(Pair<?,?> pair) throws Exception {
                    return pair.toString(start, separator, end);
                }
            };
        }
    }
}

package com.googlecode.totallylazy;

import java.util.Map;
import java.util.concurrent.Callable;

import static com.googlecode.totallylazy.Callers.call;
import static com.googlecode.totallylazy.Functions.returns;
import static com.googlecode.totallylazy.Lazy.lazy;
import static com.googlecode.totallylazy.Sequences.sequence;

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
    public static Function<Pair, String> asString(final String seperator) {
        return new Function<Pair, String>() {
            @Override
            public String call(Pair pair) throws Exception {
                return pair.toString(seperator);
            }
        };
    }

    public static <A, B, C> Function<Pair<A, B>, Pair<B, C>> reduceLeftShift(Callable2<A, B, C> callable) {
        return Pair.<A, B, C>reduceLeftShift().flip().apply(callable);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <NewF> Pair<NewF, S> map(final Function<? super F, ? extends NewF> callable) {
        return pair(first.map(callable), second);
    }

    public <R> Pair<R, S> first(Function<? super F, ? extends R> map) {
        return pair(first.then(map), second);
    }

    public <R> Pair<F,R> second(Function<? super S, ? extends R> map) {
        return pair(first, second.then(map));
    }

    public static class functions {
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
            return new Function<Pair<F1, S>, Pair<F2, S>>() {
                @Override
                public Pair<F2, S> call(Pair<F1, S> pair) throws Exception {
                    return pair(newFirst, pair.second());
                }
            };
        }
        public static <F1, F2, S> Function<Pair<F1, S>, Pair<F2, S>> replaceFirst(final F2 newFirst, final Class<S> aClass) {
            return replaceFirst(newFirst);
        }

        public static <F1, F2, S> Function<Pair<F1, S>, Pair<F2, S>> replaceFirst(final Function<F1,F2> map) {
            return new Function<Pair<F1, S>, Pair<F2, S>>() {
                @Override
                public Pair<F2, S> call(Pair<F1, S> pair) throws Exception {
                    return pair(map.call(pair.first()), pair.second());
                }
            };
        }

        public static <F1, F2, S> Function<Pair<F1, S>, Pair<F2, S>> replaceFirst(final Function<F1,F2> map, Class<S> aClass) {
            return replaceFirst(map);
        }

        public static <F, S1, S2> Function<Pair<F, S1>, Pair<F, S2>> replaceSecond(final S2 newSecond) {
            return new Function<Pair<F, S1>, Pair<F, S2>>() {
                @Override
                public Pair<F, S2> call(Pair<F, S1> pair) throws Exception {
                    return pair(pair.first(), newSecond);
                }
            };
        }

        public static <F, S1, S2> Function<Pair<F, S1>, Pair<F, S2>> replaceSecond(final Class<F> aClass, final S2 map) {
            return replaceSecond(map);
        }

        public static <F, S1, S2> Function<Pair<F, S1>, Pair<F, S2>> replaceSecond(final Function<S1,S2> map) {
            return new Function<Pair<F, S1>, Pair<F, S2>>() {
                @Override
                public Pair<F, S2> call(Pair<F, S1> pair) throws Exception {
                    return pair(pair.first(), map.call(pair.second()));
                }
            };
        }

        public static <F, S1, S2> Function<Pair<F, S1>, Pair<F, S2>> replaceSecond(Class<F> aClass, final Function<S1,S2> newSecond) {
            return replaceSecond(newSecond);
        }

        public static <F, S> Function<S, Pair<F, S>> toPairWithFirst(final F first) {
            return new Function<S, Pair<F, S>>() {
                @Override
                public Pair<F, S> call(S second) throws Exception {
                    return pair(first, second);
                }
            };
        }

        public static <F, S> Function<F, Pair<F, S>> toPairWithSecond(final S second) {
            return new Function<F, Pair<F, S>>() {
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

        public static Function<Pair<?,?>, String> pairToString(final String separator) {
            return new Function<Pair<?,?>, String>() {
                public String call(Pair<?,?> pair) throws Exception {
                    return pair.toString(separator);
                }
            };
        }

        public static Function<Pair<?,?>, String> pairToString(final String start, final String separator, final String end) {
            return new Function<Pair<?,?>, String>() {
                public String call(Pair<?,?> pair) throws Exception {
                    return pair.toString(start, separator, end);
                }
            };
        }
    }
}

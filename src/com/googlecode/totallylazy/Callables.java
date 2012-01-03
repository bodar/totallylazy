package com.googlecode.totallylazy;

import com.googlecode.totallylazy.comparators.AscendingComparator;
import com.googlecode.totallylazy.comparators.DescendingComparator;

import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.util.Comparator;
import java.util.Iterator;
import java.util.concurrent.Callable;

import static com.googlecode.totallylazy.Methods.method;
import static com.googlecode.totallylazy.Sequences.sequence;

public final class Callables {
    public static <T> Callable1<Value<T>, T> value() {
        return new Callable1<Value<T>, T>() {
            public T call(Value<T> value) throws Exception {
                return value.value();
            }
        };
    }

    public static <T, R> Callable1<T, R> doThen(final Callable1<? super T, Void> runnable, final Callable<R> callable) {
        return new Callable1<T, R>() {
            public R call(T t) throws Exception {
                runnable.call(t);
                return callable.call();
            }
        };
    }

    public static <T, R> Callable1<T, R> asCallable1(final Callable<? extends R> callable) {
        return new Callable1<T, R>() {
            public R call(T t) throws Exception {
                return callable.call();
            }
        };
    }

    public static <T> Callable1<T, T> nullGuard(final Callable1<? super T, T> callable) {
        return new Callable1<T, T>() {
            public T call(T o) throws Exception {
                if (o == null) return null;
                return callable.call(o);
            }
        };
    }

    public static <T> Callable1<Sequence<T>, Sequence<T>> reduceAndShift(final Callable2<T, T, T> action) {
        return new Callable1<Sequence<T>, Sequence<T>>() {
            public final Sequence<T> call(final Sequence<T> values) throws Exception {
                return values.tail().add(values.reduceLeft(action));
            }
        };
    }

    public static <T, S> Callable1<T, S> cast(final Class<S> aClass) {
        return new Callable1<T, S>() {
            @SuppressWarnings("unchecked")
            public final S call(final T t) throws Exception {
                return (S) t;
            }
        };
    }

    public static Callable1<Object, Class> toClass() {
        return new Callable1<Object, Class>() {
            public final Class call(final Object o) throws Exception {
                return o.getClass();
            }
        };
    }

    public static <T, R extends Comparable<R>> Comparator<T> ascending(final Callable1<T, R> callable) {
        return new AscendingComparator<T, R>(callable);
    }

    public static <T, R extends Comparable<R>> Comparator<T> descending(final Callable1<T, R> callable) {
        return new DescendingComparator<T, R>(callable);
    }

    public static Callable1<Object, Integer> size() {
        return length();
    }

    public static Callable1<Object, Integer> length() {
        return new Callable1<Object, Integer>() {
            public final Integer call(final Object instance) throws Exception {
                Class aClass = instance.getClass();
                if (aClass.isArray()) {
                    return Array.getLength(instance);
                }
                return sequence(method(instance, "size"), method(instance, "length")).
                        filter(Predicates.<Method>some()).
                        map(Callables.<Method>value()).
                        headOption().
                        map(Methods.<Integer>invokeOn(instance)).
                        getOrElse(Callables.<Integer>callThrows(new UnsupportedOperationException("Does not support fields yet")));
            }
        };
    }


    public static <T> Callable1<Sequence<T>, Sequence<T>> realise() {
        return new Callable1<Sequence<T>, Sequence<T>>() {
            public final Sequence<T> call(final Sequence<T> sequence) throws Exception {
                return sequence.realise();
            }
        };
    }

    public static <T> Callable1<First<T>, T> first(Class<T> aClass) {
        return first();
    }

    public static <T> Callable1<First<T>, T> first() {
        return new Callable1<First<T>, T>() {
            public final T call(final First<T> first) throws Exception {
                return first.first();
            }
        };
    }

    public static <F, S, R> Callable1<Pair<F, S>, Pair<R, S>> first(final Callable1<? super F, R> firstCallable) {
        return new Callable1<Pair<F, S>, Pair<R, S>>() {
            public Pair<R, S> call(Pair<F, S> pair) throws Exception {
                return Pair.pair(firstCallable.call(pair.first()), pair.second());
            }
        };
    }

    public static <T> Callable1<Second<T>, T> second(Class<T> aClass) {
        return second();
    }

    public static <T> Callable1<Second<T>, T> second() {
        return new Callable1<Second<T>, T>() {
            public final T call(final Second<T> second) throws Exception {
                return second.second();
            }
        };
    }

    public static <F, S, R> Callable1<Pair<F, S>, Pair<F, R>> second(final Callable1<? super S, R> secondCallable) {
        return new Callable1<Pair<F, S>, Pair<F, R>>() {
            public Pair<F, R> call(Pair<F, S> pair) throws Exception {
                return Pair.pair(pair.first(), secondCallable.call(pair.second()));
            }
        };
    }

    public static <T> Callable1<Third<T>, T> third(Class<T> aClass) {
        return third();
    }

    public static <T> Callable1<Third<T>, T> third() {
        return new Callable1<Third<T>, T>() {
            public final T call(final Third<T> third) throws Exception {
                return third.third();
            }
        };
    }

    public static <F, S, T, R> Callable1<Triple<F, S, T>, Triple<F, S, R>> third(final Callable1<? super T, R> thirdCallable) {
        return new Callable1<Triple<F, S, T>, Triple<F, S, R>>() {
            public Triple<F, S, R> call(Triple<F, S, T> triple) throws Exception {
                return Triple.triple(triple.first(), triple.second(), thirdCallable.call(triple.third()));
            }
        };
    }

    public static <T> Callable1<Fourth<T>, T> fourth(Class<T> aClass) {
        return fourth();
    }

    public static <T> Callable1<Fourth<T>, T> fourth() {
        return new Callable1<Fourth<T>, T>() {
            public final T call(final Fourth<T> fourth) throws Exception {
                return fourth.fourth();
            }
        };
    }

    public static <F, S, T, Fo, R> Callable1<Quadruple<F, S, T, Fo>, Quadruple<F, S, T, R>> fourth(final Callable1<? super Fo, R> fourthCallable) {
        return new Callable1<Quadruple<F, S, T, Fo>, Quadruple<F, S, T, R>>() {
            public Quadruple<F, S, T, R> call(Quadruple<F, S, T, Fo> quadruple) throws Exception {
                return Quadruple.quadruple(quadruple.first(), quadruple.second(), quadruple.third(), fourthCallable.call(quadruple.fourth()));
            }
        };
    }

    public static <T> Callable1<Fifth<T>, T> fifth(Class<T> aClass) {
        return fifth();
    }

    public static <T> Callable1<Fifth<T>, T> fifth() {
        return new Callable1<Fifth<T>, T>() {
            public final T call(final Fifth<T> fifth) throws Exception {
                return fifth.fifth();
            }
        };
    }

    public static <F, S, T, Fo, Fi, R> Callable1<Quintuple<F, S, T, Fo, Fi>, Quintuple<F, S, T, Fo, R>> fifth(final Callable1<? super Fi, R> callable) {
        return new Callable1<Quintuple<F, S, T, Fo, Fi>, Quintuple<F, S, T, Fo, R>>() {
            public Quintuple<F, S, T, Fo, R> call(Quintuple<F, S, T, Fo, Fi> quintuple) throws Exception {
                return Quintuple.quintuple(quintuple.first(), quintuple.second(), quintuple.third(), quintuple.fourth(), callable.call(quintuple.fifth()));
            }
        };
    }

    public static <T> Callable1<Iterable<T>, T> head() {
        return new Callable1<Iterable<T>, T>() {
            public final T call(final Iterable<T> iterable) throws Exception {
                return Sequences.head(iterable);
            }
        };
    }

    public static <T> Callable1<Object, String> asString(Class<T> aClass) {
        return Callables.asString();
    }

    public static Callable1<Object, String> asString() {
        return new Callable1<Object, String>() {
            public final String call(final Object value) {
                return value.toString();
            }
        };
    }

    private static final Callable2<Integer, Object, Integer> HASH_CODE = new Callable2<Integer, Object, Integer>() {
        public Integer call(Integer hash, Object value) throws Exception {
            return hash * (value == null ? 19 : value.hashCode() == 0 ? 19 : value.hashCode());
        }
    };

    public static Callable2<Integer, Object, Integer> asHashCode() {
        return HASH_CODE;
    }


    public static <T> Callable1<Iterable<T>, Iterator<T>> asIterator() {
        return new Callable1<Iterable<T>, Iterator<T>>() {
            public Iterator<T> call(Iterable<T> iterable) throws Exception {
                return iterable.iterator();
            }
        };
    }

    public static <T> Callable1<Iterator<T>, Iterable<T>> asIterable() {
        return new Callable1<Iterator<T>, Iterable<T>>() {
            public final Iterable<T> call(final Iterator<T> iterator) throws Exception {
                return Sequences.forwardOnly(iterator);
            }
        };
    }

    public static <T> Callable<T> returns(final T t) {
        return new Callable<T>() {
            public final T call() throws Exception {
                return t;
            }
        };
    }

    public static <T, R> Callable1<T, R> ignoreAndReturn(final R r) {
        return new Callable1<T, R>() {
            public R call(T t) throws Exception {
                return r;
            }
        };
    }

    public static <T> Callable1<T, T> returnArgument() {
        return new Callable1<T, T>() {
            public final T call(final T value) {
                return value;
            }
        };
    }

    public static <T> Callable1<T, T> returnArgument(final Class<T> aClass) {
        return returnArgument();
    }

    public static <T> Callable<T> aNull(final Class<T> aClass) {
        return new Callable<T>() {
            public final T call() throws Exception {
                return null;
            }
        };
    }

    public static <T> Callable<T> callThrows(final Exception e) {
        return new Callable<T>() {
            public final T call() throws Exception {
                throw e;
            }
        };
    }

    public static <T> Callable<T> callThrows(final Exception e, final Class<T> aClass) {
        return callThrows(e);
    }

    public static <T> Callable1<Callable<T>, T> call() {
        return new Callable1<Callable<T>, T>() {
            public final T call(final Callable<T> callable) throws Exception {
                return callable.call();
            }
        };
    }

    public static <T> Callable1<Callable<T>, T> call(final Class<T> aClass) {
        return call();
    }

    public static <T, R, S> Function1<R, S> curry(final Callable2<T, R, S> callable, final T value) {
        return Callers.call(curry(callable), value);
    }

    public static <T, R, S> Function1<T, Function1<R, S>> curry(final Callable2<T, R, S> callable) {
        return new Function1<T, Function1<R, S>>() {
            public final Function1<R, S> call(final T t) throws Exception {
                return new Function1<R, S>() {
                    public final S call(final R r) throws Exception {
                        return callable.call(t, r);
                    }
                };
            }
        };
    }

    public static <T, R> Callable<R> curry(final Callable1<? super T, ? extends R> callable, final T value) {
        return new Callable<R>() {
            public final R call() throws Exception {
                return callable.call(value);
            }
        };
    }

    public static <T, S> Function1<T, Callable<S>> bounce(final Callable1<? super T, S> callable) {
        return new Function1<T, Callable<S>>() {
            public Callable<S> call(T t) throws Exception {
                return Callables.curry(callable, t);
            }
        };
    }

    public static <A, B, C> Function2<A, B, C> unCurry(final Callable1<? super A, ? extends Callable1<B, C>> callable) {
        return new Function2<A, B, C>() {
            public final C call(final A a, final B b) throws Exception {
                return callable.call(a).call(b);
            }
        };
    }

    public static <L> Function1<Either<L, ?>, L> left(Class<L> aClass) {
        return left();
    }

    public static <L> Function1<Either<L, ?>, L> left() {
        return new Function1<Either<L, ?>, L>() {
            public L call(Either<L, ?> either) throws Exception {
                return either.left();
            }
        };
    }

    public static <R> Function1<Either<?, R>, R> right(Class<R> aClass) {
        return right();
    }

    public static <R> Function1<Either<?, R>, R> right() {
        return new Function1<Either<?, R>, R>() {
            public R call(Either<?, R> either) throws Exception {
                return either.right();
            }
        };
    }

    public static <A, B, C> Function2<B, A, C> flip(final Callable2<? super A, ? super B, C> callable) {
        return new Function2<B, A, C>() {
            public C call(B s, A t) throws Exception {
                return callable.call(t, s);
            }
        };
    }
}
package com.googlecode.totallylazy;

import com.googlecode.totallylazy.comparators.AscendingComparator;
import com.googlecode.totallylazy.comparators.DescendingComparator;

import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;

import static com.googlecode.totallylazy.Methods.invokeOn;
import static com.googlecode.totallylazy.Methods.method;
import static com.googlecode.totallylazy.Sequences.sequence;

public final class Callables {
    public static <T> Callable1<? super Value<T>, T> value() {
        return new Callable1<Value<T>, T>() {
            public T call(Value<T> value) throws Exception {
                return value.value();
            }
        };
    }

    public static <T, R> Callable1<? super T,R> doThen(final Callable1<? super T,Void> runnable, final Callable<R> callable) {
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

    public static <T> Callable1<? super T, T> nullGuard(final Callable1<? super T, T> callable) {
        return new Callable1<T, T>() {
            public T call(T o) throws Exception {
                if(o == null) return null;
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

    public static <T> Callable1<Future<T>, T> realiseFuture() {
        return new Callable1<Future<T>, T>() {
            public final T call(final Future<T> future) throws Exception {
                return future.get();
            }
        };
    }

    public static <T> Callable1<? super First<T>, T> first(Class<T> aClass) {
        return first();
    }

    public static <T> Callable1<? super First<T>, T> first() {
        return new Callable1<First<T>, T>() {
            public final T call(final First<T> first) throws Exception {
                return first.first();
            }
        };
    }

    public static <F, S, R> Callable1<? super Pair<F, S>,Pair<R, S>> first(final Callable1<F, R> firstCallable) {
        return new Callable1<Pair<F, S>, Pair<R, S>>() {
            public Pair<R, S> call(Pair<F, S> pair) throws Exception {
                return Pair.pair(firstCallable.call(pair.first()), pair.second());
            }
        };
    }

    public static <T> Callable1<? super Second<T>, T> second(Class<T> aClass) {
        return second();
    }

    public static <T> Callable1<? super Second<T>, T> second() {
        return new Callable1<Second<T>, T>() {
            public final T call(final Second<T> second) throws Exception {
                return second.second();
            }
        };
    }

    public static <F, S, R> Callable1<? super Pair<F, S>,Pair<F, R>> second(final Callable1<S, R> secondCallable) {
        return new Callable1<Pair<F, S>, Pair<F, R>>() {
            public Pair<F, R> call(Pair<F, S> pair) throws Exception {
                return Pair.pair(pair.first(), secondCallable.call(pair.second()));
            }
        };
    }

    public static <T> Callable1<? super Iterable<T>, T> head() {
        return new Callable1<Iterable<T>, T>() {
            public final T call(final Iterable<T> iterable) throws Exception {
                return Sequences.head(iterable);
            }
        };
    }

    public static <T> Callable1<T, String> asString(Class<T> aClass) {
        return asString();
    }

    public static <T> Callable1<T, String> asString() {
        return new Callable1<T, String>() {
            public final String call(final T value) {
                return value.toString();
            }
        };
    }

    public static Callable2<Integer, Object, Integer> asHashCode() {
        return new Callable2<Integer, Object, Integer>() {
            public Integer call(Integer hash, Object value) throws Exception {
                return hash * (value == null ? 0 :value.hashCode());
            }
        };
    }


    public static <T>  Callable1<? super Iterable<T>, Iterator<T>> asIterator() {
        return new Callable1<Iterable<T>, Iterator<T>>() {
            public Iterator<T> call(Iterable<T> iterable) throws Exception {
                return iterable.iterator();
            }
        };
    }

    public static <T> Callable1<? super Iterator<T>, Iterable<T>> asIterable() {
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

    public static <T,R,S> Callable1<R, S> curry(final Callable2<T, R, S> callable, final T value) {
        return Callers.call(curry(callable), value);
    }

    public static <T,R,S> Callable1<T, Callable1<R, S>> curry(final Callable2<T, R, S> callable) {
        return new Callable1<T, Callable1<R, S>>() {
            public final Callable1<R, S> call(final T t) throws Exception {
                return new Callable1<R, S>() {
                    public final S call(final R r) throws Exception {
                        return callable.call(t, r);
                    }
                };
            }
        };
    }

    public static <T,R> Callable<R> curry(final Callable1<T, R> callable, final T value) {
        return new Callable<R>() {
            public final R call() throws Exception {
               return callable.call(value);
            }
        };
    }

    public static <T, S> Callable1<T, Callable<S>> bounce(final Callable1<? super T, S> callable) {
        return new Callable1<T, Callable<S>>() {
            public Callable<S> call(T t) throws Exception {
                return Callables.curry(callable, t);
            }
        };
    }

    public static <T, R, S> Callable2<T, R, S> unCurry(final Callable1<T, Callable1<R,S>> callable) {
        return new Callable2<T, R, S>() {
            public final S call(final T t, final R r) throws Exception {
                return callable.call(t).call(r);
            }
        };
    }

    public static <L> Callable1<Either<L, ?>, L> left(Class<L> aClass) {
        return left();
    }

    public static <L> Callable1<Either<L, ?>, L> left() {
        return new Callable1<Either<L, ?>, L>() {
            public L call(Either<L, ?> either) throws Exception {
                return either.left();
            }
        };
    }

    public static <R> Callable1<Either<?, R>, R> right(Class<R> aClass) {
        return right();
    }

    public static <R> Callable1<Either<?, R>, R> right() {
        return new Callable1<Either<?, R>, R>() {
            public R call(Either<?, R> either) throws Exception {
                return either.right();
            }
        };
    }

}
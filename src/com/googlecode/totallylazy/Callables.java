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
    public static <T> Function1<Value<T>, T> value() {
        return new Function1<Value<T>, T>() {
            public T call(Value<T> value) throws Exception {
                return value.value();
            }
        };
    }

    public static <T, R> Function1<T, R> doThen(final Callable1<? super T, Void> runnable, final Callable<R> callable) {
        return new Function1<T, R>() {
            public R call(T t) throws Exception {
                runnable.call(t);
                return callable.call();
            }
        };
    }

    public static <T, R> Function1<T, R> asCallable1(final Callable<? extends R> callable) {
        return new Function1<T, R>() {
            public R call(T t) throws Exception {
                return callable.call();
            }
        };
    }

    public static <T> Function1<T, T> nullGuard(final Callable1<? super T, T> callable) {
        return new Function1<T, T>() {
            public T call(T o) throws Exception {
                if (o == null) return null;
                return callable.call(o);
            }
        };
    }

    public static <T> Function1<Sequence<T>, Sequence<T>> reduceAndShift(final Callable2<T, T, T> action) {
        return new Function1<Sequence<T>, Sequence<T>>() {
            public final Sequence<T> call(final Sequence<T> values) throws Exception {
                return values.tail().add(values.reduceLeft(action));
            }
        };
    }

    public static <T, S> Function1<T, S> cast(final Class<S> aClass) {
        return new Function1<T, S>() {
            public final S call(final T t) throws Exception {
                return aClass.cast(t);
            }
        };
    }

    public static Function1<Object, Class> toClass() {
        return new Function1<Object, Class>() {
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

    public static Function1<Object, Integer> size() {
        return length();
    }

    public static Function1<Object, Integer> length() {
        return new Function1<Object, Integer>() {
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


    public static <T> Function1<Sequence<T>, Sequence<T>> realise() {
        return new Function1<Sequence<T>, Sequence<T>>() {
            public final Sequence<T> call(final Sequence<T> sequence) throws Exception {
                return sequence.realise();
            }
        };
    }

    public static <T> Function1<First<T>, T> first(Class<T> aClass) {
        return first();
    }

    public static <T> Function1<First<T>, T> first() {
        return new Function1<First<T>, T>() {
            public final T call(final First<T> first) throws Exception {
                return first.first();
            }
        };
    }

    public static <F, S, R> Function1<Pair<F, S>, Pair<R, S>> first(final Callable1<? super F, R> firstCallable) {
        return new Function1<Pair<F, S>, Pair<R, S>>() {
            public Pair<R, S> call(Pair<F, S> pair) throws Exception {
                return Pair.pair(firstCallable.call(pair.first()), pair.second());
            }
        };
    }

    public static <T> Function1<Second<T>, T> second(Class<T> aClass) {
        return second();
    }

    public static <T> Function1<Second<T>, T> second() {
        return new Function1<Second<T>, T>() {
            public final T call(final Second<T> second) throws Exception {
                return second.second();
            }
        };
    }

    public static <F, S, R> Function1<Pair<F, S>, Pair<F, R>> second(final Callable1<? super S, R> secondCallable) {
        return new Function1<Pair<F, S>, Pair<F, R>>() {
            public Pair<F, R> call(Pair<F, S> pair) throws Exception {
                return Pair.pair(pair.first(), secondCallable.call(pair.second()));
            }
        };
    }

    public static <T> Function1<Third<T>, T> third(Class<T> aClass) {
        return third();
    }

    public static <T> Function1<Third<T>, T> third() {
        return new Function1<Third<T>, T>() {
            public final T call(final Third<T> third) throws Exception {
                return third.third();
            }
        };
    }

    public static <F, S, T, R> Function1<Triple<F, S, T>, Triple<F, S, R>> third(final Callable1<? super T, R> thirdCallable) {
        return new Function1<Triple<F, S, T>, Triple<F, S, R>>() {
            public Triple<F, S, R> call(Triple<F, S, T> triple) throws Exception {
                return Triple.triple(triple.first(), triple.second(), thirdCallable.call(triple.third()));
            }
        };
    }

    public static <T> Function1<Fourth<T>, T> fourth(Class<T> aClass) {
        return fourth();
    }

    public static <T> Function1<Fourth<T>, T> fourth() {
        return new Function1<Fourth<T>, T>() {
            public final T call(final Fourth<T> fourth) throws Exception {
                return fourth.fourth();
            }
        };
    }

    public static <F, S, T, Fo, R> Function1<Quadruple<F, S, T, Fo>, Quadruple<F, S, T, R>> fourth(final Callable1<? super Fo, R> fourthCallable) {
        return new Function1<Quadruple<F, S, T, Fo>, Quadruple<F, S, T, R>>() {
            public Quadruple<F, S, T, R> call(Quadruple<F, S, T, Fo> quadruple) throws Exception {
                return Quadruple.quadruple(quadruple.first(), quadruple.second(), quadruple.third(), fourthCallable.call(quadruple.fourth()));
            }
        };
    }

    public static <T> Function1<Fifth<T>, T> fifth(Class<T> aClass) {
        return fifth();
    }

    public static <T> Function1<Fifth<T>, T> fifth() {
        return new Function1<Fifth<T>, T>() {
            public final T call(final Fifth<T> fifth) throws Exception {
                return fifth.fifth();
            }
        };
    }

    public static <F, S, T, Fo, Fi, R> Function1<Quintuple<F, S, T, Fo, Fi>, Quintuple<F, S, T, Fo, R>> fifth(final Callable1<? super Fi, R> callable) {
        return new Function1<Quintuple<F, S, T, Fo, Fi>, Quintuple<F, S, T, Fo, R>>() {
            public Quintuple<F, S, T, Fo, R> call(Quintuple<F, S, T, Fo, Fi> quintuple) throws Exception {
                return Quintuple.quintuple(quintuple.first(), quintuple.second(), quintuple.third(), quintuple.fourth(), callable.call(quintuple.fifth()));
            }
        };
    }

    public static <T> Function1<Iterable<T>, T> head() {
        return new Function1<Iterable<T>, T>() {
            public final T call(final Iterable<T> iterable) throws Exception {
                return Sequences.head(iterable);
            }
        };
    }

    public static <T> Function1<T, String> asString(Class<T> aClass) {
        return Callables.asString();
    }

    public static <T> Function1<T, String> asString() {
        return new Function1<T, String>() {
            public final String call(final T value) {
                return value.toString();
            }
        };
    }

    private static final Function2<Integer, Object, Integer> HASH_CODE = new Function2<Integer, Object, Integer>() {
        public Integer call(Integer hash, Object value) throws Exception {
            return hash * (value == null ? 19 : value.hashCode() == 0 ? 19 : value.hashCode());
        }
    };

    public static Function2<Integer, Object, Integer> asHashCode() {
        return HASH_CODE;
    }


    public static <T> Function1<Iterable<T>, Iterator<T>> asIterator() {
        return new Function1<Iterable<T>, Iterator<T>>() {
            public Iterator<T> call(Iterable<T> iterable) throws Exception {
                return iterable.iterator();
            }
        };
    }

    public static <T> Function1<Iterator<T>, Iterable<T>> asIterable() {
        return new Function1<Iterator<T>, Iterable<T>>() {
            public final Iterable<T> call(final Iterator<T> iterator) throws Exception {
                return Sequences.forwardOnly(iterator);
            }
        };
    }

    public static <T> Function<T> returns(final T t) {
        return new Function<T>() {
            public final T call() throws Exception {
                return t;
            }
        };
    }

    public static <T, R> Function1<T, R> ignoreAndReturn(final R r) {
        return new Function1<T, R>() {
            public R call(T t) throws Exception {
                return r;
            }
        };
    }

    public static <T> Function1<T, T> returnArgument() {
        return new Function1<T, T>() {
            public final T call(final T value) {
                return value;
            }
        };
    }

    public static <T> Function1<T, T> returnArgument(final Class<T> aClass) {
        return returnArgument();
    }

    public static <T> Function<T> aNull(final Class<T> aClass) {
        return new Function<T>() {
            public final T call() throws Exception {
                return null;
            }
        };
    }

    public static <T> Function<T> callThrows(final Exception e) {
        return new Function<T>() {
            public final T call() throws Exception {
                throw e;
            }
        };
    }

    public static <T> Function<T> callThrows(final Exception e, final Class<T> aClass) {
        return callThrows(e);
    }

    public static <T> Function1<Callable<T>, T> call() {
        return new Function1<Callable<T>, T>() {
            public final T call(final Callable<T> callable) throws Exception {
                return callable.call();
            }
        };
    }

    public static <T> Function1<Callable<T>, T> call(final Class<T> aClass) {
        return call();
    }

    public static <T, R, S> Function1<R, S> curry(final Callable2<T, R, S> callable, final T value) {
        return Callers.call(curry(callable), value);
    }

    public static <A, B, C> Function1<A, Function1<B, C>> curry(final Callable2<A, B, C> callable) {
        return new Function1<A, Function1<B, C>>() {
            public final Function1<B, C> call(final A a) throws Exception {
                return new Function1<B, C>() {
                    public final C call(final B b) throws Exception {
                        return callable.call(a, b);
                    }
                };
            }
        };
    }

    public static <A, B> Function<B> curry(final Callable1<? super A, ? extends B> callable, final A value) {
        return new Function<B>() {
            public final B call() throws Exception {
                return callable.call(value);
            }
        };
    }

    public static <A, B> Function1<A, Callable<B>> bounce(final Callable1<? super A, B> callable) {
        return new Function1<A, Callable<B>>() {
            public Callable<B> call(A a) throws Exception {
                return Callables.curry(callable, a);
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

    public static <A, B, C> Function1<A, C> compose(final Callable1<A, B> first, final Callable1<B, C> second) {
        return new Function1<A, C>() {
            @Override
            public C call(A a) throws Exception {
                return second.call(first.call(a));
            }
        };
    }

    public static <A,B> Function1<A,B> interruptable(final Function1<A,B> function){
        return new Function1<A, B>() {
            @Override
            public B call(A a) throws Exception {
                if(Thread.interrupted()){
                    throw new InterruptedException();
                }
                return function.call(a);
            }
        };
    }
}
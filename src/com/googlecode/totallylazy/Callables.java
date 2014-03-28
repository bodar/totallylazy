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
    public static <T> Function<Value<T>, T> value() {
        return new Function<Value<T>, T>() {
            public T call(Value<T> value) throws Exception {
                return value.value();
            }
        };
    }

    public static <T> Function<Value<T>, T> value(Class<T> aClass) {
        return value();
    }

    public static <T, R> Function<T, R> asFunction(final Callable<? extends R> callable) {
        return new Function<T, R>() {
            public R call(T t) throws Exception {
                return callable.call();
            }
        };
    }

    public static <T> Unary<T> nullGuard(final Function<? super T, ? extends T> callable) {
        return new Unary<T>() {
            public T call(T o) throws Exception {
                if (o == null) return null;
                return callable.call(o);
            }
        };
    }

    public static <T> Unary<Sequence<T>> reduceAndShift(final Function2<? super T, ? super T, ? extends T> action) {
        return new Unary<Sequence<T>>() {
            public final Sequence<T> call(final Sequence<T> values) throws Exception {
                return values.tail().append(values.reduceLeft(action));
            }
        };
    }

    public static <T, S> Function<T, S> cast(final Class<? extends S> aClass) {
        return new Function<T, S>() {
            public final S call(final T t) throws Exception {
                return aClass.cast(t);
            }
        };
    }

    public static <T, S> Function<T, S> cast() {
        return new Function<T, S>() {
            public S call(T t) throws Exception {
                return Unchecked.cast(t);
            }
        };
    }

    public static Function<Object, Class<?>> toClass() {
        return new Function<Object, Class<?>>() {
            public final Class<?> call(final Object o) throws Exception {
                if(o == null) return Void.class;
                return o.getClass();
            }
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
        return new Function<Object, Integer>() {
            public final Integer call(final Object instance) throws Exception {
                Class aClass = instance.getClass();
                if (aClass.isArray()) {
                    return Array.getLength(instance);
                }
                return sequence(method(instance, "size"), method(instance, "length")).
                        flatMap(identity(Method.class)).
                        headOption().
                        map(Methods.<Integer>invokeOn(instance)).
                        getOrElse(Callables.<Integer>callThrows(new UnsupportedOperationException("Does not support fields yet")));
            }
        };
    }


    public static <T> Unary<Sequence<T>> realise() {
        return new Unary<Sequence<T>>() {
            public final Sequence<T> call(final Sequence<T> sequence) throws Exception {
                return sequence.realise();
            }
        };
    }

    public static <T> Function<First<T>, T> first(Class<T> aClass) {
        return first();
    }

    public static <T> Function<First<T>, T> first() {
        return new Function<First<T>, T>() {
            public final T call(final First<T> first) throws Exception {
                return first.first();
            }
        };
    }

    public static <T> Function<Iterable<T>, T> last(Class<T> t) {
        return last();
    }

    public static <T> Function<Iterable<T>, T> last() {
        return new Function<Iterable<T>, T>() {
            @Override
            public T call(Iterable<T> ts) throws Exception {
                return Sequences.last(ts);
            }
        };
    }

    public static <F, S, R> Function<Pair<F, S>, Pair<R, S>> first(final Function<? super F, ? extends R> firstCallable, Class<S> sClass) {
        return first(firstCallable);
    }

    public static <F, S, R> Function<Pair<F, S>, Pair<R, S>> first(final Function<? super F, ? extends R> firstCallable) {
        return new Function<Pair<F, S>, Pair<R, S>>() {
            public Pair<R, S> call(Pair<F, S> pair) throws Exception {
                return Pair.pair(firstCallable.call(pair.first()), pair.second());
            }
        };
    }

    public static <T> Function<Second<T>, T> second(Class<T> aClass) {
        return second();
    }

    public static <T> Function<Second<T>, T> second() {
        return new Function<Second<T>, T>() {
            public final T call(final Second<T> second) throws Exception {
                return second.second();
            }
        };
    }

    public static <F, S, R> Function<Pair<F, S>, Pair<F, R>> second(final Function<? super S, ? extends R> secondCallable) {
        return new Function<Pair<F, S>, Pair<F, R>>() {
            public Pair<F, R> call(Pair<F, S> pair) throws Exception {
                return Pair.pair(pair.first(), secondCallable.call(pair.second()));
            }
        };
    }

    public static <T> Function<Third<T>, T> third(Class<T> aClass) {
        return third();
    }

    public static <T> Function<Third<T>, T> third() {
        return new Function<Third<T>, T>() {
            public final T call(final Third<T> third) throws Exception {
                return third.third();
            }
        };
    }

    public static <F, S, T, R> Function<Triple<F, S, T>, Triple<F, S, R>> third(final Function<? super T, ? extends R> thirdCallable) {
        return new Function<Triple<F, S, T>, Triple<F, S, R>>() {
            public Triple<F, S, R> call(Triple<F, S, T> triple) throws Exception {
                return Triple.triple(triple.first(), triple.second(), thirdCallable.call(triple.third()));
            }
        };
    }

    public static <T> Function<Fourth<T>, T> fourth(Class<T> aClass) {
        return fourth();
    }

    public static <T> Function<Fourth<T>, T> fourth() {
        return new Function<Fourth<T>, T>() {
            public final T call(final Fourth<T> fourth) throws Exception {
                return fourth.fourth();
            }
        };
    }

    public static <F, S, T, Fo, R> Function<Quadruple<F, S, T, Fo>, Quadruple<F, S, T, R>> fourth(final Function<? super Fo, ? extends R> fourthCallable) {
        return new Function<Quadruple<F, S, T, Fo>, Quadruple<F, S, T, R>>() {
            public Quadruple<F, S, T, R> call(Quadruple<F, S, T, Fo> quadruple) throws Exception {
                return Quadruple.quadruple(quadruple.first(), quadruple.second(), quadruple.third(), fourthCallable.call(quadruple.fourth()));
            }
        };
    }

    public static <T> Function<Fifth<T>, T> fifth(Class<T> aClass) {
        return fifth();
    }

    public static <T> Function<Fifth<T>, T> fifth() {
        return new Function<Fifth<T>, T>() {
            public final T call(final Fifth<T> fifth) throws Exception {
                return fifth.fifth();
            }
        };
    }

    public static <F, S, T, Fo, Fi, R> Function<Quintuple<F, S, T, Fo, Fi>, Quintuple<F, S, T, Fo, R>> fifth(final Function<? super Fi, ? extends R> callable) {
        return new Function<Quintuple<F, S, T, Fo, Fi>, Quintuple<F, S, T, Fo, R>>() {
            public Quintuple<F, S, T, Fo, R> call(Quintuple<F, S, T, Fo, Fi> quintuple) throws Exception {
                return Quintuple.quintuple(quintuple.first(), quintuple.second(), quintuple.third(), quintuple.fourth(), callable.call(quintuple.fifth()));
            }
        };
    }

    public static <T> Function<Iterable<T>, T> head() {
        return new Function<Iterable<T>, T>() {
            public final T call(final Iterable<T> iterable) throws Exception {
                return Sequences.head(iterable);
            }
        };
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
        return new Function<Iterable<T>, Iterator<T>>() {
            public Iterator<T> call(Iterable<T> iterable) throws Exception {
                return iterable.iterator();
            }
        };
    }

    public static <T> Function<Iterator<T>, Iterable<T>> asIterable() {
        return new Function<Iterator<T>, Iterable<T>>() {
            public final Iterable<T> call(final Iterator<T> iterator) throws Exception {
                return Sequences.forwardOnly(iterator);
            }
        };
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

    public static <T> Unary<T> returnArgument() {
        return Functions.identity();
    }

    public static <T> Unary<T> returnArgument(final Class<T> aClass) {
        return returnArgument();
    }

    public static <T> Returns<T> aNull(final Class<T> aClass) {
        return new Returns<T>() {
            public final T call() throws Exception {
                return null;
            }
        };
    }

    public static <T> Returns<T> callThrows(final Exception e) {
        return new Returns<T>() {
            public final T call() throws Exception {
                throw e;
            }
        };
    }

    public static <T> Returns<T> callThrows(final Exception e, final Class<T> aClass) {
        return callThrows(e);
    }

    public static <T> Function<Callable<T>, T> call() {
        return new Function<Callable<T>, T>() {
            public final T call(final Callable<T> callable) throws Exception {
                return callable.call();
            }
        };
    }

    public static <T> Function<Callable<T>, T> call(final Class<T> aClass) {
        return call();
    }

    public static <T, R> Function<Function<T, R>, R> callWith(final T value) {
        return new Function<Function<T, R>, R>() {
            public final R call(final Function<T, R> callable) throws Exception {
                return callable.call(value);
            }
        };
    }

    public static <A, B, C> Returns<C> deferApply(final Function2<? super A, ? super B, ? extends C> callable, final A a, final B b) {
        return new Returns<C>() {
            @Override
            public C call() throws Exception {
                return callable.call(a, b);
            }
        };
    }

    public static <A, B> Returns<B> deferApply(final Function<? super A, ? extends B> callable, final A value) {
        return new Returns<B>() {
            public final B call() throws Exception {
                return callable.call(value);
            }
        };
    }

    public static <A, B> Function<A, Returns<B>> deferReturn(final Function<? super A, ? extends B> callable) {
        return new Function<A, Returns<B>>() {
            public Returns<B> call(A a) throws Exception {
                return Callables.deferApply(callable, a);
            }
        };
    }

    public static <A, B, C> Function<B, C> apply(final Function2<? super A, ? super B, ? extends C> callable, final A value) {
        return Functions.apply(callable, value);
    }

    public static <A, B, C, D> Function2<B, C, D> apply(final Callable3<? super A, ? super B, ? super C, ? extends D> callable, final A value) {
        return Functions.apply(callable, value);
    }

    public static <A, B, C> Function<A, Function<B, C>> curry(final Function2<? super A, ? super B, ? extends C> callable) {
        return Unchecked.cast(callable);
    }

    public static <A, B, C, D> Function<A, Function<B, Function<C, D>>> curry(final Callable3<? super A, ? super B, ? super C, ? extends D> callable) {
        return Functions.function(callable);
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
        return new Function<Either<L, ?>, L>() {
            public L call(Either<L, ?> either) throws Exception {
                return either.left();
            }
        };
    }

    public static <R> Function<Either<?, R>, R> right(Class<R> aClass) {
        return right();
    }

    public static <R> Function<Either<?, R>, R> right() {
        return new Function<Either<?, R>, R>() {
            public R call(Either<?, R> either) throws Exception {
                return either.right();
            }
        };
    }

    public static <A, B, C> Function2<B, A, C> flip(final Function2<? super A, ? super B, ? extends C> callable) {
        return new Function2<B, A, C>() {
            public C call(B s, A t) throws Exception {
                return callable.call(t, s);
            }
        };
    }

    public static <A, B, C> Function<A, C> compose(final Function<? super A, ? extends B> first, final Function<? super B, ? extends C> second) {
        return new Function<A, C>() {
            @Override
            public C call(A a) throws Exception {
                return second.call(first.call(a));
            }
        };
    }

    public static <A, B> Function<A, B> compose(final Function<? super A, ?> ignoreResult, final Callable<? extends B> callable) {
        return new Function<A, B>() {
            public B call(A a) throws Exception {
                ignoreResult.call(a);
                return callable.call();
            }
        };
    }

    public static <A, B> Function<A, B> doThen(final Function<? super A, ?> ignoreResult, final Callable<? extends B> callable) {
        return compose(ignoreResult, callable);
    }

    public static <A, B> Returns<B> compose(final Callable<? extends A> first, final Function<? super A, ? extends B> second) {
        return new Returns<B>() {
            @Override
            public B call() throws Exception {
                return second.call(first.call());
            }
        };
    }


    public static <A, B> Function<A, B> interruptable(final Function<? super A, ? extends B> function) {
        return Functions.interruptable(function);
    }

    public static <A, B, C> Function<Pair<A, B>, C> pair(final Function2<? super A, ? super B, ? extends C> function) {
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

    public static <T> Unary<T> replace(final Predicate<? super T> predicate, final Function<? super T, ? extends T> callable) {
        return when(predicate, callable);
    }

    public static <T> Unary<T> when(final Predicate<? super T> predicate, final Function<? super T, ? extends T> callable) {
        return new Unary<T>() {
            @Override
            public T call(T value) throws Exception {
                return predicate.matches(value) ? callable.call(value) : value;
            }
        };
    }
}
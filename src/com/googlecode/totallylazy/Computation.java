package com.googlecode.totallylazy;

import com.googlecode.totallylazy.callables.LazyCallable1;
import com.googlecode.totallylazy.iterators.SegmentIterator;

import java.util.Iterator;
import java.util.NoSuchElementException;

import static com.googlecode.totallylazy.Computation.computation;
import static com.googlecode.totallylazy.Function1.constant;
import static com.googlecode.totallylazy.Pair.pair;
import static com.googlecode.totallylazy.callables.LazyCallable1.lazy;

public class Computation<T> extends Sequence<T> implements Segment<T, Computation<T>>{
    private final T value;
    private final LazyCallable1<T, Computation<T>> next;

    private Computation(T value, Callable1<T, Computation<T>> next) {
        this.value = value;
        this.next = lazy(next);
    }

    public static <T> Computation<T> computation(T value, Callable1<T, Computation<T>> next) {
        return new Computation<T>(value, next);
    }

    public static <T> Computation<T> computation(T value, Computation<T> next) {
        return new Computation<T>(value, Function1.<T, Computation<T>>constant(next));
    }

    public static <T> Computation<T> iterate(final Callable1<? super T, ? extends T> callable, final T t) {
        return computation(t, new Callable1<T, Computation<T>>() {
            @Override
            public Computation<T> call(T t) throws Exception {
                return computation(callable.call(t), this);
            }
        });
    }

    public static <T> Callable1<T, Computation<T>> generate(final Callable1<T, T> callable) {
        return new Callable1<T, Computation<T>>() {
            @Override
            public Computation<T> call(T value) throws Exception {
                return computation(callable.call(value), this);
            }
        };
    }

    public static <T> Callable1<Pair<T, T>, Computation<Pair<T, T>>> generate(final Callable2<T, T, T> callable) {
        return new Callable1<Pair<T, T>, Computation<Pair<T, T>>>() {
            @Override
            public Computation<Pair<T, T>> call(Pair<T, T> p) throws Exception {
                return computation(pair(p.second(), callable.call(p.first(), p.second())), this);
            }
        };
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public T head() {
        return value;
    }

    @Override
    public Computation<T> tail() throws NoSuchElementException {
        return next.apply(value);
    }

    @Override
    public Iterator<T> iterator() {
        return new SegmentIterator<T, Computation<T>>(this);
    }

    public void forget() {
        next.forget();
    }
}

package com.googlecode.totallylazy;

import com.googlecode.totallylazy.iterators.SegmentIterator;

import java.util.Iterator;
import java.util.NoSuchElementException;

import static com.googlecode.totallylazy.callables.LazyCallable1.lazy;

public class Computation<T> extends Sequence<T> implements Segment<T, Computation<T>>{
    private final T value;
    private final Function1<T, Computation<T>> next;

    public Computation(T value, Callable1<T, Computation<T>> next) {
        this.value = value;
        this.next = lazy(next);
    }

    public static <T> Computation<T> computation(T value, Callable1<T, Computation<T>> next) {
        return new Computation<T>(value, next);
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

    @Override
    public String toString() {
        return toString("::");
    }
}

package com.googlecode.totallylazy;

import java.util.Iterator;
import java.util.concurrent.Callable;

import static com.googlecode.totallylazy.Sequences.sequence;
import static com.googlecode.totallylazy.Unchecked.cast;

public class Some<T> extends Option<T> {
    private final T value;

    private Some(T value) {
        this.value = value;
    }

    public static <T> Some<T> some(T t) {
        if (t == null) {
            throw new IllegalArgumentException("some(T) can not be null");
        }
        return new Some<T>(t);
    }

    public Iterator<T> iterator() {
        return sequence(value).iterator();
    }

    @Override
    public T get() {
        return value;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public T getOrElse(T other) {
        return get();
    }

    @Override
    public T getOrElse(Callable<? extends T> callable) {
        return get();
    }

    @Override
    public T getOrNull() {
        return get();
    }

    @Override
    public <S> Option<S> map(Callable1<? super T, ? extends S> callable) {
        return option(Callers.call(callable, get()));
    }

    @Override
    public <S> Option<S> flatMap(Callable1<? super T, ? extends Option<? extends S>> callable) {
        return cast(Callers.call(callable, get()));
    }

    @Override
    public <S> S fold(S seed, Callable2<? super S, ? super T, ? extends S> callable) {
        return Callers.call(callable, seed, get());
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof Some && ((Some) o).value().equals(value());
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @Override
    public String toString() {
        return "some(" + value + ")";
    }
}

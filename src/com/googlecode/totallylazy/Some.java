package com.googlecode.totallylazy;

import java.util.Iterator;
import java.util.concurrent.Callable;

import static com.googlecode.totallylazy.Either.right;
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
    public Option<T> orElse(Option<T> other) {
        return this;
    }

    @Override
    public Option<T> orElse(Callable<? extends Option<T>> other) {
        return this;
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
    public <E extends Exception> T getOrThrow(E e) throws E {
        return get();
    }

    @Override
    public <S> Option<S> map(Function<? super T, ? extends S> callable) {
        return option(callable.apply(get()));
    }

    @Override
    public Option<T> each(Function<? super T, ?> callable) {
        callable.apply(get());
        return this;
    }

    @Override
    public <S> Option<S> flatMap(Function<? super T, ? extends Option<? extends S>> callable) {
        return cast(callable.apply(get()));
    }

    @Override
    public Option<T> filter(Predicate<? super T> predicate) {
        return predicate.matches(value) ? this : Option.<T>none();
    }

    @Override
    public <S> S fold(S seed, BiFunction<? super S, ? super T, ? extends S> callable) {
        return Callers.call(callable, seed, get());
    }

    @Override
    public <L> Either<L, T> toEither(L value) {
        return right(this.value);
    }

    public Sequence<T> join(Iterable<? extends T> iterable) {
        return Sequences.cons(value, iterable);
    }

    @Override
    public boolean contains(T instance) {
        return value.equals(instance);
    }

    @Override
    public boolean exists(Predicate<? super T> predicate) {
        return predicate.matches(value);
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
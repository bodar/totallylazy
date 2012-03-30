package com.googlecode.totallylazy;

import java.util.concurrent.Callable;

import static com.googlecode.totallylazy.Callers.call;
import static com.googlecode.totallylazy.Function1.identity;
import static com.googlecode.totallylazy.Sequences.sequence;

public abstract class Option<T> implements Iterable<T>, Value<T>, Mappable<T, Option<?>> {
    public static <T> Option<T> option(T t) {
        if (t == null) {
            return None.none();
        }
        return new Some<T>(t);
    }

    public static <T> Option<T> some(T t) {
        return new Some<T>(t);
    }

    public static <T> Option<T> none() {
        return new None<T>();
    }

    public static <T> Option<T> none(Class<T> aClass) {
        return None.none(aClass);
    }

    public abstract T get();

    public abstract boolean isEmpty();

    public T value() {
        return get();
    }

    public final T getOrElse(T other) {
        return isEmpty() ? other : get();
    }

    public final T getOrElse(Callable<? extends T> callable) {
        return isEmpty() ? call(callable) : get();
    }

    public final T getOrNull() {
        return isEmpty() ? null : get();
    }

    @Override
    public final <S> Option<S> map(Callable1<? super T, ? extends S> callable) {
        return isEmpty() ? Option.<S>none() : option(Callers.call(callable, get()));
    }

    public final <S> Option<S> flatMap(Callable1<? super T, ? extends Option<S>> callable) {
        return isEmpty() ? Option.<S>none() : Callers.call(callable, get());
    }

    public <S> S fold(final S seed, final Callable2<? super S, ? super T, ? extends S> callable) {
        return isEmpty() ? seed : Callers.call(callable, seed, get());
    }

    public Sequence<T> toSequence() {
        return sequence(this);
    }

    public static <T> Option<T> flatten(Option<? extends Option<T>> option) {
        return option.flatMap(Function1.<Option<T>>identity());
    }
}
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
        return Some.some(t);
    }

    public static <T> Option<T> some(T t) {
        return Some.some(t);
    }

    public static <T> Option<T> none() {
        return None.none();
    }

    public static <T> Option<T> none(Class<T> aClass) {
        return None.none(aClass);
    }

    public T value() {
        return get();
    }

    public abstract T get();

    public abstract boolean isEmpty();

    public abstract T getOrElse(T other);

    public abstract T getOrElse(Callable<? extends T> callable);

    public abstract T getOrNull();

    public abstract <S> Option<S> map(Callable1<? super T, ? extends S> callable);

    public abstract <S> Option<S> flatMap(Callable1<? super T, ? extends Option<S>> callable);

    public abstract <S> S fold(final S seed, final Callable2<? super S, ? super T, ? extends S> callable);

    public Sequence<T> toSequence() {
        return sequence(this);
    }

    public static <T> Option<T> flatten(Option<? extends Option<T>> option) {
        return option.flatMap(Function1.<Option<T>>identity());
    }
}
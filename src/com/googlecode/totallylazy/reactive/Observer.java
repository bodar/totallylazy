package com.googlecode.totallylazy.reactive;

import com.googlecode.totallylazy.Function;
import com.googlecode.totallylazy.Functor;
import com.googlecode.totallylazy.Predicate;
import com.googlecode.totallylazy.Value;

import java.util.NoSuchElementException;

import static com.googlecode.totallylazy.Unchecked.cast;

public interface Observer<T> {
    void step(State<T> state);
}

interface Error<A> extends State<A> {
    Throwable throwable();

    static <A> Error<A> error(Throwable throwable) {
        return () -> throwable;
    }
}

interface Complete<A> extends State<A> {
    static <A> Complete<A> complete() {
        return new Complete<A>() { };
    }
}
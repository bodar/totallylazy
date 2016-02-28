package com.googlecode.totallylazy.reactive;

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
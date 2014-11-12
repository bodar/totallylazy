package com.googlecode.totallylazy;

import com.googlecode.totallylazy.annotations.tailrec;

import java.util.NoSuchElementException;

public interface Trampoline<T> extends Returns<T> {
    default boolean done() { return true; }

    default Trampoline<T> next() { throw new NoSuchElementException(); }

    static <T> Trampoline<T> done(T value) { return () -> value; }

    static <T> Trampoline<T> more(Returns<? extends Trampoline<T>> next) {
        return new Trampoline<T>() {
            @Override public boolean done() { return false; }

            @Override public T call() throws Exception { return trampoline(this); }

            @Override public Trampoline<T> next() { return next.apply(); }

            @tailrec
            T trampoline(Trampoline<T> trampoline) throws Exception {
                if(trampoline.done()) return trampoline.call();
                return trampoline(trampoline.next());
            }
        };
    }
}
package com.googlecode.totallylazy;

import org.junit.Test;

import java.util.NoSuchElementException;


public class FlatMapIterator<T, S> extends Iterator<S> {
    private final java.util.Iterator<T> iterator;
    private final Callable1<T, Iterable<S>> callable;
    private java.util.Iterator<S> currentIterator = null;

    public FlatMapIterator(java.util.Iterator<T> iterator, Callable1<T, Iterable<S>> callable) {
        this.iterator = iterator;
        this.callable = callable;
    }

    public boolean hasNext() {
        if (currentIterator == null) {
            if (iterator.hasNext()) {
                try {
                    currentIterator = callable.call(iterator.next()).iterator();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                return hasNext();
            }
            return false;
        }

        if (currentIterator.hasNext()) {
            return true;
        }

        currentIterator = null;
        return hasNext();
    }

    public S next() {
        if (hasNext()) {
            return currentIterator.next();
        }
        throw new NoSuchElementException();
    }
}

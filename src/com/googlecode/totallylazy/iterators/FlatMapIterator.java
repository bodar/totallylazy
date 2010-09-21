package com.googlecode.totallylazy.iterators;

import com.googlecode.totallylazy.*;

import java.util.Iterator;
import java.util.NoSuchElementException;


public class FlatMapIterator<T, S> extends LazyIterator<S> {
    private final Iterator<T> iterator;
    private final Callable1<T, Iterable<S>> callable;
    private Iterator<S> currentIterator = null;

    public FlatMapIterator(Iterator<T> iterator, Callable1<T, Iterable<S>> callable) {
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

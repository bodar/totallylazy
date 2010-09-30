package com.googlecode.totallylazy.iterators;

import com.googlecode.totallylazy.Callable1;

import java.util.Iterator;
import java.util.NoSuchElementException;

import static com.googlecode.totallylazy.Callables.call;


public class FlatMapIterator<T, S> extends ReadOnlyIterator<S> {
    private final Iterator<T> iterator;
    private final Callable1<? super T, Iterable<S>> callable;
    private Iterator<S> currentIterator = null;

    public FlatMapIterator(Iterator<T> iterator, Callable1<? super T, Iterable<S>> callable) {
        this.iterator = iterator;
        this.callable = callable;
    }

    public boolean hasNext() {
        if (currentIterator == null) {
            if (iterator.hasNext()) {
                currentIterator = call(callable, iterator.next()).iterator();
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

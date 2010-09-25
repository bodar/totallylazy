package com.googlecode.totallylazy.iterators;

import com.googlecode.totallylazy.Callable1;
import com.googlecode.totallylazy.Sequence;

import java.util.Iterator;
import java.util.NoSuchElementException;

import static com.googlecode.totallylazy.Callables.call;


public class JoinIterator<T> extends ReadOnlyIterator<T> {
    private final Iterator<Iterator<T>> iterators;
    private Iterator<T> currentIterator = null;

    public JoinIterator(Iterator<Iterator<T>> iterators) {
        this.iterators = iterators;
    }

    public boolean hasNext() {
        if (currentIterator == null) {
            if (iterators.hasNext()) {
                currentIterator = iterators.next();
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

    public T next() {
        if (hasNext()) {
            return currentIterator.next();
        }
        throw new NoSuchElementException();
    }
}

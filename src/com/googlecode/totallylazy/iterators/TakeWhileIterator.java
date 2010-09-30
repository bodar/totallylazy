package com.googlecode.totallylazy.iterators;

import com.googlecode.totallylazy.Predicate;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class TakeWhileIterator<T> extends ReadOnlyIterator<T> {
    private final Iterator<T> iterator;
    private final Predicate<T> predicate;
    private T current;
    private boolean continueIterating = true;

    public TakeWhileIterator(Iterator<T> iterator, Predicate<T> predicate) {
        this.iterator = iterator;
        this.predicate = predicate;
    }

    public boolean hasNext() {
        if (!iterator.hasNext()) {
            return false;
        }

        if (current == null) {
            current = iterator.next();
            continueIterating = predicate.matches(current);
        }

        return continueIterating;
    }

    public T next() {
        if(current != null){
            T result = current;
            current = null;
            return result;
        }

        if (hasNext()) {
            T result = current;
            current = null;
            return result;
        }

        throw new NoSuchElementException();
    }
}

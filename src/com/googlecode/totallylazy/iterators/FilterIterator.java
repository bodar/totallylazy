package com.googlecode.totallylazy.iterators;

import com.googlecode.totallylazy.Iterator;
import com.googlecode.totallylazy.Predicate;

import java.util.NoSuchElementException;

public class FilterIterator<T> extends Iterator<T> {
    private final java.util.Iterator<T> iterator;
    private final Predicate<T> predicate;
    private T matched = null;

    public FilterIterator(java.util.Iterator<T> iterator, Predicate<T> predicate) {
        this.iterator = iterator;
        this.predicate = predicate;
    }

    public boolean hasNext() {
        if (matched == null) {
            while (iterator.hasNext()) {
                T item = iterator.next();
                if (predicate.matches(item)) {
                    matched = item;
                    return true;
                }
            }
            return false;
        }
        return true;
    }

    public T next() {
        if (matched != null) {
            T result = matched;
            matched = null;
            return result;
        }
        if (!hasNext()) {
            throw new NoSuchElementException();
        }
        return next();
    }
}
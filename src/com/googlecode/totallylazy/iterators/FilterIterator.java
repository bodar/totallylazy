package com.googlecode.totallylazy.iterators;

import com.googlecode.totallylazy.Predicate;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class FilterIterator<T> extends ReadOnlyIterator<T> {
    private final Iterator<T> iterator;
    private final Predicate<T> predicate;
    private T matched = null;

    public FilterIterator(Iterator<T> iterator, Predicate<T> predicate) {
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
        if (hasNext()) {
            return popMatched();
        }
        throw new NoSuchElementException();
    }

    private T popMatched() {
        T result = matched;
        matched = null;
        return result;
    }
}
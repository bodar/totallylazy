package com.googlecode.totallylazy.iterators;

import java.util.Iterator;

public class FlattenIterator<T> extends StatefulIterator<T> {
    private final Iterator<? extends Iterator<? extends T>> iterator;
    private Iterator<? extends T> currentIterator = new EmptyIterator<T>();

    public FlattenIterator(Iterator<? extends Iterator<? extends T>> iterator) {
        this.iterator = iterator;
    }

    public T getNext() {
        Iterator<? extends T> iterator = getCurrentIterator();
        if (iterator.hasNext()) {
            return iterator.next();
        }
        return finished();
    }

    public Iterator<? extends T> getCurrentIterator() {
        while (!currentIterator.hasNext()){
            if(!iterator.hasNext()){
                return new EmptyIterator<T>();
            }
            currentIterator = iterator.next();
        }
        return currentIterator;
    }
}
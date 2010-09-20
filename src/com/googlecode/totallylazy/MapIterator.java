package com.googlecode.totallylazy;

public class MapIterator<T,S> extends Iterator<S> {
    private final java.util.Iterator<T> iterator;
    private final Callable1<T, S> callable;

    public MapIterator(java.util.Iterator<T> iterator, Callable1<T, S> callable) {
        this.iterator = iterator;
        this.callable = callable;
    }

    public boolean hasNext() {
        return iterator.hasNext();
    }

    public S next() {
        try {
            return callable.call(iterator.next());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

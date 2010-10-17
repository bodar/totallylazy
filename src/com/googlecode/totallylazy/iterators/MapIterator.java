package com.googlecode.totallylazy.iterators;

import com.googlecode.totallylazy.Callable1;

import java.util.Iterator;

import static com.googlecode.totallylazy.Callers.call;

public final class MapIterator<T, S> extends ReadOnlyIterator<S> {
    private final Iterator<T> iterator;
    private final Callable1<? super T, S> callable;

    public MapIterator(final Iterator<T> iterator, final Callable1<? super T, S> callable) {
        this.iterator = iterator;
        this.callable = callable;
    }

    public final boolean hasNext() {
        return iterator.hasNext();
    }

    public final S next() {
        return call(callable, iterator.next());
    }
}

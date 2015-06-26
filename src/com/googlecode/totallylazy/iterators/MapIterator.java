package com.googlecode.totallylazy.iterators;

import com.googlecode.totallylazy.functions.Function1;

import java.util.Iterator;

import static com.googlecode.totallylazy.Callers.call;

public final class MapIterator<T, S> extends ReadOnlyIterator<S> {
    private final Iterator<? extends T> iterator;
    private final Function1<? super T, ? extends S> callable;

    public MapIterator(final Iterator<? extends T> iterator, final Function1<? super T, ? extends S> callable) {
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

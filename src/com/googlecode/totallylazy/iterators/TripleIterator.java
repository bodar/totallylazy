package com.googlecode.totallylazy.iterators;

import com.googlecode.totallylazy.Triple;

import java.util.Iterator;
import java.util.NoSuchElementException;

public final class TripleIterator<F, S, T> extends ReadOnlyIterator<Triple<F, S, T>> {
    private final Iterator<? extends F> first;
    private final Iterator<? extends S> second;
    private final Iterator<? extends T> third;

    public TripleIterator(final Iterator<? extends F> first, final Iterator<? extends S> second, final Iterator<? extends T> third) {
        this.first = first;
        this.second = second;
        this.third = third;
    }

    public final boolean hasNext() {
        return first.hasNext() && second.hasNext() && third.hasNext();
    }

    public final Triple<F, S, T> next() {
        if (hasNext()) {
            return Triple.triple(first.next(), second.next(), third.next());
        }
        throw new NoSuchElementException();
    }
}

package com.googlecode.totallylazy.iterators;

import com.googlecode.totallylazy.Pair;

import java.util.Iterator;
import java.util.NoSuchElementException;

import static com.googlecode.totallylazy.Pair.pair;

public final class PairIterator<F, S> extends ReadOnlyIterator<Pair<F, S>> {
    private final Iterator<? extends F> left;
    private final Iterator<? extends S> right;

    public PairIterator(final Iterator<? extends F> left, final Iterator<? extends S> right) {
        this.left = left;
        this.right = right;
    }

    public final boolean hasNext() {
        return left.hasNext() && right.hasNext();
    }

    public final Pair<F, S> next() {
        if (hasNext()) {
            return pair(left.next(), right.next());
        }
        throw new NoSuchElementException();
    }
}

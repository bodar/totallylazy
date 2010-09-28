package com.googlecode.totallylazy.iterators;

import com.googlecode.totallylazy.Sequence;
import com.googlecode.totallylazy.Pair;
import static com.googlecode.totallylazy.Pair.pair;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class ZipIterator<F, S> extends ReadOnlyIterator<Pair<F, S>> {
    private final Iterator<F> left;
    private final Iterator<S> right;

    public ZipIterator(Iterator<F> left, Iterator<S> right) {
        this.left = left;
        this.right = right;
    }

    public boolean hasNext() {
        return left.hasNext() && right.hasNext();
    }

    public Pair<F, S> next() {
        if (hasNext()) {
            return pair(left.next(), right.next());
        }
        throw new NoSuchElementException();
    }
}

package com.googlecode.totallylazy.iterators;

import com.googlecode.totallylazy.Iterators;
import com.googlecode.totallylazy.Sequence;

import java.util.Iterator;

import static com.googlecode.totallylazy.Sequences.sequence;

public final class TransposeIterator<T> extends ReadOnlyIterator<Sequence<T>> {
    private final Sequence<Iterator<T>> iterators;

    public TransposeIterator(Iterable<? extends Iterator<? extends T>> iterators) {
        this.iterators = sequence(iterators).realise().unsafeCast();
    }

    public final boolean hasNext() {
        return iterators.forAll(Iterators.<T>hasNext());
    }

    public final Sequence<T> next() {
        return iterators.map(Iterators.<T>next()).realise();
    }
}

package com.googlecode.totallylazy.iterators;

import com.googlecode.totallylazy.Iterators;
import com.googlecode.totallylazy.Seq;

import java.util.Iterator;

import static com.googlecode.totallylazy.Sequences.sequence;

public final class TransposeIterator<T> implements ReadOnlyIterator<Seq<T>> {
    private final Seq<Iterator<T>> iterators;

    public TransposeIterator(Iterable<? extends Iterator<? extends T>> iterators) {
        this.iterators = sequence(iterators).realise().unsafeCast();
    }

    public final boolean hasNext() {
        return iterators.forAll(Iterators.<T>hasNext());
    }

    public final Seq<T> next() {
        return iterators.map(Iterators.<T>next()).realise();
    }
}

package com.googlecode.totallylazy.iterators;

import com.googlecode.totallylazy.Iterators;
import com.googlecode.totallylazy.Pair;
import com.googlecode.totallylazy.Sequence;
import com.googlecode.totallylazy.Sequences;

import java.util.Iterator;
import java.util.NoSuchElementException;

import static com.googlecode.totallylazy.Pair.pair;

public final class TransposeIterator<T> extends ReadOnlyIterator<Sequence<T>> {
    private final Sequence<Iterator<T>> iterators;

    public TransposeIterator(Iterable<Iterator<T>> iterators) {
        this.iterators = Sequences.sequence(iterators).memorise();
    }

    public final boolean hasNext() {
        return iterators.forAll(Iterators.<T>hasNext());

    }

    public final Sequence<T> next() {
        if (hasNext()) {
            return iterators.map(Iterators.<T>next()).memorise();
        }
        throw new NoSuchElementException();
    }
}

package com.googlecode.totallylazy.iterators;

import com.googlecode.totallylazy.Iterators;
import com.googlecode.totallylazy.Pair;
import com.googlecode.totallylazy.Sequence;
import com.googlecode.totallylazy.Sequences;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import static com.googlecode.totallylazy.Pair.pair;

public final class TransposeIterator<T> extends ReadOnlyIterator<Sequence<T>> {
    private final List<Iterator<T>> iterators;

    public TransposeIterator(List<Iterator<T>> iterators) {
        this.iterators = iterators;
    }

    public final boolean hasNext() {
        for (Iterator<T> iterator : iterators) {
            if(!iterator.hasNext()){
                return false;
            }
        }
        return true;
    }

    public final Sequence<T> next() {
        if (hasNext()) {
            List<T> result = new ArrayList<T>();
            for (Iterator<T> iterator : iterators) {
                result.add(iterator.next());
            }
            return Sequences.sequence(result);
        }
        throw new NoSuchElementException();
    }
}

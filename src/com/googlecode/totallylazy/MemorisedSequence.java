package com.googlecode.totallylazy;

import com.googlecode.totallylazy.iterators.MemorisedIterator;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static com.googlecode.totallylazy.Unchecked.cast;
import static java.util.Collections.synchronizedList;

public final class MemorisedSequence<T> extends Sequence<T> {
    private final List<T> memory = synchronizedList(new ArrayList<T>());
    private final Iterable<? extends T> iterable;
    private Iterator<? extends T> iterator = null;

    public MemorisedSequence(final Iterable<? extends T> iterable) {
        this.iterable = iterable;
    }

    public final Iterator<T> iterator() {
        return new MemorisedIterator<T>(getIterator(), memory);
    }

    public final void forget() {
        synchronized (memory) {
            memory.clear();
            iterator = null;
        }
    }

    private Iterator<T> getIterator() {
        synchronized (memory) {
            if (iterator == null) {
                iterator = iterable.iterator();
            }
            return cast(iterator);
        }
    }
}

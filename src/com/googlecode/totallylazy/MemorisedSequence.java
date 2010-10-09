package com.googlecode.totallylazy;

import com.googlecode.totallylazy.iterators.EmptyIterator;
import com.googlecode.totallylazy.iterators.MemorisedIterator;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static java.util.Collections.synchronizedList;

public class MemorisedSequence<T> extends Sequence<T> {
    private final List<T> memory = synchronizedList(new ArrayList<T>());
    private final Iterable<T> iterable;
    private Iterator<T> iterator = null;

    public MemorisedSequence(Iterable<T> iterable) {
        this.iterable = iterable;
    }

    public Iterator<T> iterator() {
        return new MemorisedIterator<T>(getIterator(), memory);
    }

    public void forget() {
        synchronized (memory) {
            memory.clear();
            iterator = null;
        }
    }

    public Iterator<T> getIterator() {
        synchronized (memory) {
            if (iterator == null) {
                iterator = iterable.iterator();
            }
            return iterator;
        }
    }
}

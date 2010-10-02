package com.googlecode.totallylazy;

import com.googlecode.totallylazy.iterators.MemoriseIterator;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import static java.util.Collections.synchronizedList;

public class MemoriseSequence<T> extends Sequence<T> {
    private final Iterator<T> iterator;
    private final List<T> memory = synchronizedList(new ArrayList<T>());

    public MemoriseSequence(Iterable<T> iterable) {
        this(iterable.iterator());
    }

    public MemoriseSequence(Iterator<T> iterator) {
        this.iterator = iterator;
    }

    public Iterator<T> iterator() {
        return new MemoriseIterator<T>(iterator, memory);
    }
}

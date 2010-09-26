package com.googlecode.totallylazy;

import com.googlecode.totallylazy.iterators.MemoriseIterator;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MemoriseSequence<T> extends Sequence<T> {
    private final Iterable<T> iterable;
    private final List<T> memory = new ArrayList<T>();

    public MemoriseSequence(Iterable<T> iterable) {
        this.iterable = iterable;
    }

    public Iterator<T> iterator() {
        return new MemoriseIterator<T>(iterable.iterator(), memory); 
    }
}
package com.googlecode.totallylazy;

import java.util.Iterator;

public class ForwardOnlySequence<T> extends Sequence<T> {
    private final Iterator<T> iterator;

    public ForwardOnlySequence(Iterator<T> iterator) {
        this.iterator = iterator;
    }

    public final Iterator<T> iterator() {
        return iterator;
    }
}

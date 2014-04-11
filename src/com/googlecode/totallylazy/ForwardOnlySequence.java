package com.googlecode.totallylazy;

import java.util.Iterator;

import static com.googlecode.totallylazy.Unchecked.cast;

public final class ForwardOnlySequence<T> extends AbstractSequence<T> {
    private final Iterator<? extends T> iterator;

    public ForwardOnlySequence(final Iterator<? extends T> iterator) {
        this.iterator = iterator;
    }

    public final Iterator<T> iterator() {
        return cast(iterator);
    }
}

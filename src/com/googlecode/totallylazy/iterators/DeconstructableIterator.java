package com.googlecode.totallylazy.iterators;

import com.googlecode.totallylazy.Deconstructable;

public class DeconstructableIterator<T, Self extends Deconstructable<T, Self>> extends ReadOnlyIterator<T> {
    private Deconstructable<T, Self> list;

    public DeconstructableIterator(Deconstructable<T, Self> list) {
        this.list = list;
    }

    @Override
    public boolean hasNext() {
        return !list.isEmpty();
    }

    @Override
    public T next() {
        final T head = list.head();
        list = list.tail();
        return head;
    }
}

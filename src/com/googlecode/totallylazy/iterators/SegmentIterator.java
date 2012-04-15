package com.googlecode.totallylazy.iterators;

import com.googlecode.totallylazy.Segment;

public class SegmentIterator<T, Self extends Segment<T, Self>> extends ReadOnlyIterator<T> {
    private Segment<T, Self> segment;

    public SegmentIterator(Segment<T, Self> segment) {
        this.segment = segment;
    }

    @Override
    public boolean hasNext() {
        return !segment.isEmpty();
    }

    @Override
    public T next() {
        final T head = segment.head();
        segment = segment.tail();
        return head;
    }
}

package com.googlecode.totallylazy;

import com.googlecode.totallylazy.collections.AbstractCollection;

public abstract class AbstractSequence<T> extends AbstractCollection<T> implements Sequence<T> {
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Iterable) return Sequences.equalTo(this, (Iterable<?>) obj);
        return obj instanceof Segment && Segment.methods.equalTo(this, (Segment<?>) obj);
    }

    public String toString() {
        return Sequences.toString(this);
    }

    @Override
    public Sequence<T> deleteAll(final Iterable<? extends T> iterable) {
        return Sequence.super.deleteAll(iterable);
    }
}

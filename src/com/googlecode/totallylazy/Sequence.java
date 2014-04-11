package com.googlecode.totallylazy;

import com.googlecode.totallylazy.collections.AbstractCollection;

public abstract class Sequence<T> extends AbstractCollection<T> implements Seq<T> {
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Iterable) return Sequences.equalTo(this, (Iterable<?>) obj);
        return obj instanceof Segment && Segment.methods.equalTo(this, (Segment<?>) obj);
    }

    public String toString() {
        return Sequences.toString(this);
    }

    @Override
    public Seq<T> deleteAll(final Iterable<? extends T> iterable) {
        return Seq.super.deleteAll(iterable);
    }
}

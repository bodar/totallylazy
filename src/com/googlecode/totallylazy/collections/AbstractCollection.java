package com.googlecode.totallylazy.collections;

import com.googlecode.totallylazy.Sequence;

import java.util.Collection;

public abstract class AbstractCollection<T> extends ReadOnlyCollection<T> implements PersistentCollection<T> {
    public abstract Sequence<T> toSequence();

    @Override
    public Object[] toArray() {
        return toSequence().toArray();
    }

    @Override
    public <T1> T1[] toArray(T1[] a) {
        return toSequence().toArray(a);
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return toSequence().containsAll(c);
    }
}

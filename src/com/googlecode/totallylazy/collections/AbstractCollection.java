package com.googlecode.totallylazy.collections;

import com.googlecode.totallylazy.Sequence;

import java.util.Collection;

import static com.googlecode.totallylazy.Predicates.in;
import static com.googlecode.totallylazy.Predicates.not;
import static com.googlecode.totallylazy.Sequences.sequence;

public abstract class AbstractCollection<T> extends ReadOnlyCollection<T> implements PersistentCollection<T> {
    @Override
    public Sequence<T> toSequence() {
        return sequence(this);
    }

    @Override
    public Object[] toArray() {
        return toSequence().toList().toArray();
    }

    @Override
    public <R> R[] toArray(R[] a) {
        return toSequence().toList().toArray(a);
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return toSequence().containsAll(c);
    }

    @Override
    public PersistentCollection<T> deleteAll(Iterable<? extends T> items) {
        return filter(not(in(items)));
    }
}

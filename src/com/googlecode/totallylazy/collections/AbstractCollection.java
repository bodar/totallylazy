package com.googlecode.totallylazy.collections;

import com.googlecode.totallylazy.Sequence;
import com.googlecode.totallylazy.Unchecked;

import java.lang.reflect.Array;
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
    public T[] toArray(final Class<?> aClass) {
        return toArray(Unchecked.<T[]>cast(Array.newInstance(aClass, 0)));
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

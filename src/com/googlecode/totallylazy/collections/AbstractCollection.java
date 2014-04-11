package com.googlecode.totallylazy.collections;

import com.googlecode.totallylazy.Sequence;

import static com.googlecode.totallylazy.Callables.asHashCode;

public abstract class AbstractCollection<T> implements PersistentCollection<T> {
    @Override
    public Sequence<T> toSequence() {
        return PersistentCollection.super.toSequence();
    }

    @Override
    public String toString() {
        return appendTo(new StringBuilder(), "(", ",", ")").toString();
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof AbstractCollection && toSequence().equals(((AbstractCollection) other).toSequence());
    }

    // Thread-safe Racy Single Check Idiom (Effective Java 2nd Edition p.284)
    private int hashCode;

    @Override
    public int hashCode() {
        if (hashCode == 0) {
            hashCode = toSequence().fold(31, asHashCode());
        }
        return hashCode;
    }


}

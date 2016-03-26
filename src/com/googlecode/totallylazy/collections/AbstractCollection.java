package com.googlecode.totallylazy.collections;

import com.googlecode.totallylazy.Sequence;
import com.googlecode.totallylazy.Sequences;
import com.googlecode.totallylazy.Unchecked;

import java.lang.reflect.Array;
import java.util.Collection;

import static com.googlecode.totallylazy.functions.Callables.asHashCode;
import static com.googlecode.totallylazy.predicates.Predicates.in;
import static com.googlecode.totallylazy.predicates.Predicates.not;
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
    public String toString() {
        return appendTo(new StringBuilder(), "(", ",", ")").toString();
    }

    public String toString(final String separator) {
        return appendTo(new StringBuilder(), separator).toString();
    }

    public String toString(final String start, final String separator, final String end) {
        return appendTo(new StringBuilder(), start, separator, end).toString();
    }

    public <A extends Appendable> A appendTo(A appendable) {
        return Sequences.appendTo(this, appendable);
    }

    public <A extends Appendable> A appendTo(A appendable, final String separator) {
        return Sequences.appendTo(this, appendable, separator);
    }

    public <A extends Appendable> A appendTo(A appendable, final String start, final String separator, final String end) {
        return Sequences.appendTo(this, appendable,start, separator, end);
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof AbstractCollection && toSequence().equals(other);
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

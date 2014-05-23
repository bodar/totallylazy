package com.googlecode.totallylazy.collections;

import com.googlecode.totallylazy.Filterable;
import com.googlecode.totallylazy.Predicate;
import com.googlecode.totallylazy.Segment;
import com.googlecode.totallylazy.Seq;
import com.googlecode.totallylazy.Sequence;
import com.googlecode.totallylazy.Sequences;
import com.googlecode.totallylazy.Unchecked;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.NoSuchElementException;

import static com.googlecode.totallylazy.Predicates.in;
import static com.googlecode.totallylazy.Predicates.not;
import static com.googlecode.totallylazy.Sequences.sequence;

public interface PersistentCollection<T> extends PersistentContainer<T>, Collection<T>, Segment<T>, Filterable<T> {
    @Override
    PersistentCollection<T> empty();

    @Override
    PersistentCollection<T> cons(T t);

    @Override
    PersistentCollection<T> tail() throws NoSuchElementException;

    PersistentCollection<T> delete(T t);

    default PersistentCollection<T> deleteAll(Iterable<? extends T> items) {
        return filter(not(in(items)));
    }

    @Override
    PersistentCollection<T> filter(Predicate<? super T> predicate);

    default Seq<T> toSequence() {
        return sequence(this);
    }

    @Override
    default Object[] toArray() {
        return toSequence().toList().toArray();
    }

    @Override
    default <R> R[] toArray(R[] a) { return toSequence().toList().toArray(a); }

    default T[] toArray(final Class<?> aClass) { return toArray(Unchecked.<T[]>cast(Array.newInstance(aClass, 0))); }

    @Override
    default boolean containsAll(Collection<?> c) {
        return toSequence().containsAll(c);
    }

    default String toString(final String separator) {
        return appendTo(new StringBuilder(), separator).toString();
    }

    default String toString(final String start, final String separator, final String end) {
        return appendTo(new StringBuilder(), start, separator, end).toString();
    }

    default <A extends Appendable> A appendTo(A appendable) {
        return Sequences.appendTo(this, appendable);
    }

    default <A extends Appendable> A appendTo(A appendable, final String separator) {
        return Sequences.appendTo(this, appendable, separator);
    }

    default <A extends Appendable> A appendTo(A appendable, final String start, final String separator, final String end) {
        return Sequences.appendTo(this, appendable,start, separator, end);
    }

    /** @deprecated Mutation not supported. Replaced by {@link PersistentCollection#cons(T)} */
    @Override @Deprecated
    default boolean add(T e) {
        throw new IllegalMutationException();
    }

    /** @deprecated Mutation not supported. Replaced by {@link PersistentCollection#cons(T)} */
    @Override @Deprecated
    default boolean addAll(Collection<? extends T> c) {
        throw new IllegalMutationException();
    }

    /** @deprecated Mutation not supported. Replaced by {@link PersistentCollection#delete(T)} */
    @Override @Deprecated
    default boolean remove(Object o) {
        throw new IllegalMutationException();
    }

    /** @deprecated Mutation not supported. Replaced by {@link PersistentCollection#deleteAll(Iterable)} */
    @Override @Deprecated
    default boolean removeAll(Collection<?> c) {
        throw new IllegalMutationException();
    }

    /** @deprecated Mutation not supported. Replaced by {@link PersistentCollection#filter(com.googlecode.totallylazy.Predicate)} */
    @Override @Deprecated
    default boolean retainAll(Collection<?> c) {
        throw new IllegalMutationException();
    }

    /** @deprecated Mutation not supported. Replaced by {@link PersistentCollection#empty()} */
    @Override @Deprecated
    default void clear() {
        throw new IllegalMutationException();
    }

}

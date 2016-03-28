package com.googlecode.totallylazy.collections;

import com.googlecode.totallylazy.functions.Function1;
import com.googlecode.totallylazy.Filterable;
import com.googlecode.totallylazy.Foldable;
import com.googlecode.totallylazy.Functor;
import com.googlecode.totallylazy.Option;
import com.googlecode.totallylazy.predicates.Predicate;
import com.googlecode.totallylazy.Segment;
import com.googlecode.totallylazy.Sequence;

import java.util.Set;

public interface PersistentSet<T> extends Set<T>, Iterable<T>, Segment<T>, PersistentCollection<T>, Foldable<T> {
    Option<T> lookup(T value);

    @Override
    PersistentSet<T> empty();

    @Override
    PersistentSet<T> cons(T head);

    @Override
    PersistentSet<T> delete(T value);

    PersistentList<T> toPersistentList();

    Sequence<T> toSequence();

    Set<T> toSet();



    class functions extends Segment.functions {
        public static <T> Function1<PersistentSet<T>,Option<T>> get(final T value) {
            return set -> set.lookup(value);
        }
    }
}
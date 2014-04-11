package com.googlecode.totallylazy.collections;

import com.googlecode.totallylazy.Function;
import com.googlecode.totallylazy.Filterable;
import com.googlecode.totallylazy.Foldable;
import com.googlecode.totallylazy.Functor;
import com.googlecode.totallylazy.Option;
import com.googlecode.totallylazy.Predicate;
import com.googlecode.totallylazy.Segment;
import com.googlecode.totallylazy.Sequence;

import java.util.Collection;
import java.util.Set;

@SuppressWarnings("deprecated")
public interface PersistentSet<T> extends Set<T>, Iterable<T>, Segment<T>, PersistentCollection<T>, Functor<T>, Foldable<T>, Filterable<T> {
    Option<T> lookup(T value);

    Option<T> find(Predicate<? super T> predicate);

    @Override
    PersistentSet<T> empty();

    @Override
    PersistentSet<T> cons(T head);

    @Override
    PersistentSet<T> delete(T value);

    @Override
    PersistentSet<T> filter(Predicate<? super T> predicate);

    <NewT> PersistentSet<NewT> map(Function<? super T, ? extends NewT> transformer);

    @Override
    default boolean containsAll(Collection<?> c) { return PersistentCollection.super.containsAll(c); }

    PersistentList<T> toPersistentList();

    Sequence<T> toSequence();

    Set<T> toSet();

    /** @deprecated Mutation not supported. Replaced by {@link PersistentCollection#cons(T)} */
    @Override @Deprecated
    default boolean add(T e) { return PersistentCollection.super.add(e); }

    /** @deprecated Mutation not supported. Replaced by {@link PersistentCollection#cons(T)} */
    @Override @Deprecated
    default boolean addAll(Collection<? extends T> c) {
        return PersistentCollection.super.addAll(c);
    }

    /** @deprecated Mutation not supported. Replaced by {@link PersistentCollection#delete(T)} */
    @Override @Deprecated
    default boolean remove(Object o) {
        return PersistentCollection.super.remove(o);
    }

    /** @deprecated Mutation not supported. Replaced by {@link PersistentCollection#deleteAll(Iterable)} */
    @Override @Deprecated
    default boolean removeAll(Collection<?> c) {
        return PersistentCollection.super.removeAll(c);
    }

    /** @deprecated Mutation not supported. Replaced by {@link PersistentCollection#filter(com.googlecode.totallylazy.Predicate)} */
    @Override @Deprecated
    default boolean retainAll(Collection<?> c) { return PersistentCollection.super.retainAll(c); }

    /** @deprecated Mutation not supported. Replaced by {@link PersistentCollection#empty()} */
    @Override @Deprecated
    default void clear() { PersistentCollection.super.clear();  }

    @Override
    default <R> R[] toArray(R[] a) { return PersistentCollection.super.toArray(a) ;}

    @Override
    default Object[] toArray() { return PersistentCollection.super.toArray(); }

    @Override
    default T[] toArray(final Class<?> aClass) { return PersistentCollection.super.toArray(aClass); }

    class functions extends Segment.functions {
        public static <T> Function<PersistentSet<T>,Option<T>> get(final T value) {
            return new Function<PersistentSet<T>, Option<T>>() {
                @Override
                public Option<T> call(PersistentSet<T> set) throws Exception {
                    return set.lookup(value);
                }
            };
        }
    }
}
package com.googlecode.totallylazy.collections;

import com.googlecode.totallylazy.Callable1;
import com.googlecode.totallylazy.Foldable;
import com.googlecode.totallylazy.Functor;
import com.googlecode.totallylazy.Option;
import com.googlecode.totallylazy.Predicate;
import com.googlecode.totallylazy.Segment;
import com.googlecode.totallylazy.Sequence;

import java.util.Set;

public interface PersistentSet<T> extends Iterable<T>, Segment<T>, PersistentCollection<T>, Functor<T>, Foldable<T> {
    Option<T> find(Predicate<? super T> predicate);

    @Override
    PersistentSet<T> empty();

    @Override
    PersistentSet<T> cons(T head);

    PersistentSet<T> put(T value);

    PersistentSet<T> remove(T value);

    PersistentSet<T> filter(Predicate<? super T> predicate);

    <NewT> PersistentSet<NewT> map(Callable1<? super T, ? extends NewT> transformer);

    PersistentList<T> toPersistentList();

    Sequence<T> toSequence();

    Set<T> toSet();
}
package com.googlecode.totallylazy.collections;

import com.googlecode.totallylazy.Callable1;
import com.googlecode.totallylazy.ImmutableCollection;
import com.googlecode.totallylazy.Option;
import com.googlecode.totallylazy.Predicate;
import com.googlecode.totallylazy.Segment;

public interface ImmutableSet<T> extends Iterable<T>, Segment<T>, ImmutableCollection<T> {
    Option<T> find(Predicate<? super T> predicate);

    @Override
    ImmutableSet<T> cons(T head);

    ImmutableSet<T> put(T value);

    ImmutableSet<T> remove(T value);

    ImmutableSet<T> filter(Predicate<? super T> predicate);

    <NewT> ImmutableSet<NewT> map(Callable1<? super T, ? extends NewT> transformer);

    ImmutableList<T> immutableList();
}
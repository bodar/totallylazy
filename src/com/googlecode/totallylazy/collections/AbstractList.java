package com.googlecode.totallylazy.collections;

import java.util.List;
import java.util.ListIterator;

import static com.googlecode.totallylazy.Predicates.in;
import static com.googlecode.totallylazy.Predicates.not;
import static com.googlecode.totallylazy.collections.ListZipper.zipper;

@SuppressWarnings("deprecation")
public abstract class AbstractList<T> extends ReadOnlyList<T> implements PersistentList<T> {
    @Override
    public int lastIndexOf(Object o) {
        return toMutableList().lastIndexOf(o);
    }

    @Override
    public ListIterator<T> listIterator() {
        return new ZipperListIterator<T>(zipper(this));
    }

    @Override
    public ListIterator<T> listIterator(int index) {
        return new ZipperListIterator<T>(zipper(this).index(index));
    }

    @Override
    public List<T> subList(int fromIndex, int toIndex) {
        throw new UnsupportedOperationException("TODO");
    }

    @Override
    public PersistentList<T> reverse() {
        return PersistentList.constructors.reverse(this);
    }

    @Override
    public List<T> toMutableList() {
        return toSequence().toList();
    }

    @Override
    public PersistentList<T> deleteAll(Iterable<? extends T> values) {
        return filter(not(in(values)));
    }
}

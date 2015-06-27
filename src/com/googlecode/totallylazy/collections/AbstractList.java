package com.googlecode.totallylazy.collections;

import java.util.List;
import java.util.ListIterator;

import static com.googlecode.totallylazy.predicates.Predicates.in;
import static com.googlecode.totallylazy.predicates.Predicates.not;

@SuppressWarnings("deprecation")
public abstract class AbstractList<T> extends ReadOnlyList<T> implements PersistentList<T> {
    @Override
    public int lastIndexOf(Object o) {
        return toMutableList().lastIndexOf(o);
    }

    @Override
    public ListIterator<T> listIterator() {
        return new ZipperListIterator<T>(zipper());
    }

    @Override
    public ListIterator<T> listIterator(int index) {
        return new ZipperListIterator<T>(zipper().index(index));
    }

    @Override
    public PersistentList<T> subList(int fromIndex, int toIndex) {
        return PersistentList.constructors.list(toMutableList().subList(fromIndex, toIndex));
    }

    @Override
    public Zipper<T> zipper() {
        return ListZipper.zipper(this);
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

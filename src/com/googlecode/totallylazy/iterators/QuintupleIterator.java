package com.googlecode.totallylazy.iterators;

import com.googlecode.totallylazy.Quintuple;

import java.util.Iterator;
import java.util.NoSuchElementException;

public final class QuintupleIterator<F, S, T, Fo, Fi> extends ReadOnlyIterator<Quintuple<F, S, T, Fo, Fi>> {
    private final Iterator<F> first;
    private final Iterator<S> second;
    private final Iterator<T> third;
    private final Iterator<Fo> fourth;
    private final Iterator<Fi> fifth;

    public QuintupleIterator(final Iterator<F> first, final Iterator<S> second, final Iterator<T> third, final Iterator<Fo> fourth, final Iterator<Fi> fifth) {
        this.first = first;
        this.second = second;
        this.third = third;
        this.fourth = fourth;
        this.fifth = fifth;
    }

    public final boolean hasNext() {
        return first.hasNext() && second.hasNext() && third.hasNext() && fourth.hasNext() && fifth.hasNext();
    }

    public final Quintuple<F, S, T, Fo, Fi> next() {
        if (hasNext()) {
            return Quintuple.quintuple(first.next(), second.next(), third.next(), fourth.next(), fifth.next());
        }
        throw new NoSuchElementException();
    }
}

package com.googlecode.totallylazy.iterators;

import com.googlecode.totallylazy.Quintuple;

import java.util.Iterator;
import java.util.NoSuchElementException;

public final class QuintupleIterator<F, S, T, Fo, Fi> implements ReadOnlyIterator<Quintuple<F, S, T, Fo, Fi>> {
    private final Iterator<? extends F> first;
    private final Iterator<? extends S> second;
    private final Iterator<? extends T> third;
    private final Iterator<? extends Fo> fourth;
    private final Iterator<? extends Fi> fifth;

    public QuintupleIterator(final Iterator<? extends F> first, final Iterator<? extends S> second, final Iterator<? extends T> third, final Iterator<? extends Fo> fourth, final Iterator<? extends Fi> fifth) {
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

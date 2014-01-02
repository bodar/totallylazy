package com.googlecode.totallylazy.iterators;

import com.googlecode.totallylazy.Quadruple;

import java.util.Iterator;
import java.util.NoSuchElementException;

public final class QuadrupleIterator<F, S, T, Fo> extends ReadOnlyIterator<Quadruple<F, S, T, Fo>> {
    private final Iterator<? extends F> first;
    private final Iterator<? extends S> second;
    private final Iterator<? extends T> third;
    private final Iterator<? extends Fo> fourth;

    public QuadrupleIterator(final Iterator<? extends F> first, final Iterator<? extends S> second, final Iterator<? extends T> third, final Iterator<? extends Fo> fourth) {
        this.first = first;
        this.second = second;
        this.third = third;
        this.fourth = fourth;
    }

    public final boolean hasNext() {
        return first.hasNext() && second.hasNext() && third.hasNext() && fourth.hasNext();
    }

    public final Quadruple<F, S, T, Fo> next() {
        if (hasNext()) {
            return Quadruple.quadruple(first.next(), second.next(), third.next(), fourth.next());
        }
        throw new NoSuchElementException();
    }
}

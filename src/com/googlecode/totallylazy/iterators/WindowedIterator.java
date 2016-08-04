package com.googlecode.totallylazy.iterators;

import com.googlecode.totallylazy.Sequence;
import com.googlecode.totallylazy.Sequences;

import java.util.Iterator;

public class WindowedIterator<T> extends StatefulIterator<Sequence<T>> {
    private Sequence<T> sequence;
    private final int step;
    private final int size;

    public WindowedIterator(Iterator<? extends T> iterator, int size) {
        this(iterator, 1, size);
    }

    public WindowedIterator(Iterator<? extends T> iterator, int step, int size) {
        this.sequence = Sequences.memorise(iterator);
        this.step = step;
        this.size = size;
    }

    @Override
    protected Sequence<T> getNext() throws Exception {
        Sequence<T> take = sequence.take(size);
        if (take.size() == size) {
            sequence = sequence.drop(step);
            return take;
        }
        return finished();
    }
}

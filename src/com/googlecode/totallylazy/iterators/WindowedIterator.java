package com.googlecode.totallylazy.iterators;

import com.googlecode.totallylazy.Seq;
import com.googlecode.totallylazy.Sequences;

import java.util.Iterator;

public class WindowedIterator<T> extends StatefulIterator<Seq<T>> {
    private Seq<T> sequence;
    private final int size;

    public WindowedIterator(Iterator<? extends T> iterator, int size) {
        this.sequence = Sequences.memorise(iterator);
        this.size = size;
    }

    @Override
    protected Seq<T> getNext() throws Exception {
        Seq<T> take = sequence.take(size);
        if (take.size() == size) {
            sequence = sequence.tail();
            return take;
        }
        return finished();
    }
}

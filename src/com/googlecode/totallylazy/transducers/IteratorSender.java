package com.googlecode.totallylazy.transducers;

import java.util.Iterator;

import static com.googlecode.totallylazy.transducers.State.Stop;

public class IteratorSender<A> implements Sender<A> {
    private final Iterator<? extends A> iterator;

    public IteratorSender(Iterator<? extends A> iterator) {this.iterator = iterator;}

    @Override
    public AutoCloseable send(Receiver<A> receiver) {
        if (receiver.start().equals(Stop)) return EMPTY_CLOSEABLE;
        while (iterator.hasNext()) {
            A value = iterator.next();
            if (receiver.next(value).equals(Stop)) break;
        }
        receiver.finish();
        return EMPTY_CLOSEABLE;
    }
}

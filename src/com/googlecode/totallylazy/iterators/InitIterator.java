package com.googlecode.totallylazy.iterators;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class InitIterator<T> extends StatefulIterator<T> {
    private final PeekingIterator<T> peekingIterator;

    public InitIterator(Iterator<? extends T> iterator) {
        this.peekingIterator = new PeekingIterator<T>(iterator);
    }

    @Override
    protected T getNext() throws Exception {
        T next = peekingIterator.next();
        try {
            peekingIterator.peek();
            return next;
        } catch (NoSuchElementException ex){
            return finished();
        }
    }
}

package com.googlecode.totallylazy.iterators;

import com.googlecode.totallylazy.Callable1;

import java.util.Iterator;

import static com.googlecode.totallylazy.Callers.call;

public class FlatMapIterator<T, S> extends StatefulIterator<S> {
    private final Iterator<T> iterator;
    private final Callable1<? super T, Iterable<S>> callable;
    private Iterator<? extends S> currentIterator = new EmptyIterator<S>();

    public FlatMapIterator(Iterator<T> iterator, Callable1<? super T, Iterable<S>> callable) {
        this.iterator = iterator;
        this.callable = callable;
    }

    public S getNext() {
        Iterator<? extends S> iterator = getCurrentIterator();
        if (iterator.hasNext()) {
            return iterator.next();
        }
        return finished();
    }

    public Iterator<? extends S> getCurrentIterator() {
        while (!currentIterator.hasNext()){
            if(!iterator.hasNext()){
                return new EmptyIterator<S>();
            }
            currentIterator = call(callable, iterator.next()).iterator();
        }

        return currentIterator;
    }
}

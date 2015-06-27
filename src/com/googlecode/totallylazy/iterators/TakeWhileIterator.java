package com.googlecode.totallylazy.iterators;

import com.googlecode.totallylazy.predicates.Predicate;

import java.util.Iterator;

public final class TakeWhileIterator<T> extends StatefulIterator<T> {
    private final Iterator<? extends T> iterator;
    private final Predicate<? super T> predicate;

    public TakeWhileIterator(final Iterator<? extends T> iterator, final Predicate<? super T> predicate) {
        this.iterator = iterator;
        this.predicate = predicate;
    }

    @Override
    public final T getNext() {
        if(iterator.hasNext()){
            T item = iterator.next();
            if(predicate.matches(item)){
                return item;
            }
        }
        return finished();
    }
}

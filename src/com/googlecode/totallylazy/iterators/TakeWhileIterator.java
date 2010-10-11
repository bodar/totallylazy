package com.googlecode.totallylazy.iterators;

import com.googlecode.totallylazy.Option;
import com.googlecode.totallylazy.Predicate;

import java.util.Iterator;

import static com.googlecode.totallylazy.Option.none;
import static com.googlecode.totallylazy.Option.some;

public class TakeWhileIterator<T> extends StatefulIterator<T> {
    private final Iterator<T> iterator;
    private final Predicate<? super T> predicate;

    public TakeWhileIterator(Iterator<T> iterator, Predicate<? super T> predicate) {
        this.iterator = iterator;
        this.predicate = predicate;
    }

    @Override
    public Option<T> getNext() {
        if(iterator.hasNext()){
            T item = iterator.next();
            if(predicate.matches(item)){
                return some(item);
            }
        }
        return none();
    }
}

package com.googlecode.totallylazy.iterators;

import com.googlecode.totallylazy.Predicate;

import java.util.Iterator;
import java.util.Queue;

public class PartitionIterator<T> extends StatefulIterator<T> {
    private final Iterator<? extends T> underlyingIterator;
    private final Predicate<? super T> predicate;
    private final Queue<T> matched;
    private final Queue<T> unmatched;

    public PartitionIterator(Iterator<? extends T> underlyingIterator, Predicate<? super T> predicate, Queue<T> matched, Queue<T> unmatched) {
        this.underlyingIterator = underlyingIterator;
        this.predicate = predicate;
        this.matched = matched;
        this.unmatched = unmatched;
    }

    protected T getNext() throws Exception {
        if (!matched.isEmpty()) return matched.remove();
        if (!underlyingIterator.hasNext()) return finished();

        T t = underlyingIterator.next();
        if (predicate.matches(t)) return t;
        unmatched.add(t);
        return getNext();
    }
}

package com.googlecode.totallylazy.iterators;

import com.googlecode.totallylazy.Predicate;
import com.googlecode.totallylazy.Option;
import static com.googlecode.totallylazy.Option.some;
import static com.googlecode.totallylazy.Option.none;

import java.util.Iterator;
import java.util.Queue;

public class PartitionIterator<T> extends StatefulIterator<T> {
    private final Iterator<T> underlyingIterator;
    private final Predicate<? super T> predicate;
    private final Queue<T> matched;
    private final Queue<T> unmatched;

    public PartitionIterator(Iterator<T> underlyingIterator, Predicate<? super T> predicate, Queue<T> matched, Queue<T> unmatched) {
        this.underlyingIterator = underlyingIterator;
        this.predicate = predicate;
        this.matched = matched;
        this.unmatched = unmatched;
    }

    protected Option<T> getNext() throws Exception {
        if (!matched.isEmpty()) {
            return some(matched.remove());
        }
        if (!underlyingIterator.hasNext()) {
            return none();
        }
        T t = underlyingIterator.next();
        if (predicate.matches(t)) {
            return some(t);
        }
        unmatched.add(t);
        return getNext();
    }
}

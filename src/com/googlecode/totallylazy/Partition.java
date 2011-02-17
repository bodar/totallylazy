package com.googlecode.totallylazy;

import static com.googlecode.totallylazy.Predicates.not;
import static com.googlecode.totallylazy.Sequences.forwardOnly;
import com.googlecode.totallylazy.iterators.PartitionIterator;

import java.util.*;

public class Partition<T> {
    private final Queue<T> matched = new LinkedList<T>();
    private final Queue<T> unmatched = new LinkedList<T>();
    private final Iterator<T> matchedIterator;
    private final Iterator<T> unmatchedIterator;

    public Partition(final Iterable<T> iterable, final Predicate<? super T> predicate) {
        final Iterator<T> underlyingIterator = iterable.iterator();
        matchedIterator = new PartitionIterator<T>(underlyingIterator, predicate, matched, unmatched);
        unmatchedIterator = new PartitionIterator<T>(underlyingIterator, not(predicate), unmatched, matched);
    }

    public ForwardOnlySequence<T> matched() {
        return forwardOnly(matchedIterator);
    }

    public ForwardOnlySequence<T> unmatched() {
        return forwardOnly(unmatchedIterator);
    }
}

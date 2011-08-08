package com.googlecode.totallylazy;

import static com.googlecode.totallylazy.Predicates.not;
import static com.googlecode.totallylazy.Sequences.forwardOnly;
import static com.googlecode.totallylazy.Sequences.memorise;

import com.googlecode.totallylazy.iterators.PartitionIterator;

import java.util.*;

public class Partition<T>{
    private final Queue<T> matched = new LinkedList<T>();
    private final Queue<T> unmatched = new LinkedList<T>();
    private final Iterator<T> matchedIterator;
    private final Iterator<T> unmatchedIterator;

    private Partition(final Iterable<T> iterable, final Predicate<? super T> predicate) {
        final Iterator<T> underlyingIterator = iterable.iterator();
        matchedIterator = new PartitionIterator<T>(underlyingIterator, predicate, matched, unmatched);
        unmatchedIterator = new PartitionIterator<T>(underlyingIterator, not(predicate), unmatched, matched);
    }

    public static <T> Pair<Sequence<T>, Sequence<T>> partition(final Iterable<T> iterable, final Predicate<? super T> predicate) {
        Partition<T> partition = new Partition<T>(iterable, predicate);
        return Pair.pair(partition.matched(), partition.unmatched());
    }

    public Sequence<T> matched() {
        return memorise(matchedIterator);
    }

    public Sequence<T> unmatched() {
        return memorise(unmatchedIterator);
    }
}

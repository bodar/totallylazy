package com.googlecode.totallylazy.matchers;

import com.googlecode.totallylazy.predicates.LogicalPredicate;

import static com.googlecode.totallylazy.Sequences.sequence;

public class HasSizePredicate<T> extends LogicalPredicate<Iterable<T>> {
    private final Number size;

    public HasSizePredicate(Number size) {
        this.size = size;
    }

    @Override
    public boolean matches(Iterable<T> iterable) {
        return size.equals(sequence(iterable).size());
    }

    @Override
    public String toString() {
         return"Iterable with size " + size;
    }
}

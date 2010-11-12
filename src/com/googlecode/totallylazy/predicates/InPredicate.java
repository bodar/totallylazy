package com.googlecode.totallylazy.predicates;

import com.googlecode.totallylazy.Predicate;
import com.googlecode.totallylazy.Sequence;

public class InPredicate<T> implements Predicate<T> {
    private final Sequence<T> sequence;

    public InPredicate(Sequence<T> sequence) {
        this.sequence = sequence;
    }

    public boolean matches(T other) {
        return sequence.contains(other);
    }

    public Sequence<T> values() {
        return sequence;
    }
}

package com.googlecode.totallylazy.predicates;

import com.googlecode.totallylazy.Predicate;
import com.googlecode.totallylazy.Sequence;
import com.googlecode.totallylazy.Sequences;

public class OrPredicate<T> extends LogicalPredicate<T> {
    private final Sequence<Predicate<T>> predicates;

    private OrPredicate(Sequence<Predicate<T>> predicates) {
        this.predicates = predicates;
    }

    public static <T> LogicalPredicate<T> or(Iterable<? extends Predicate<? super T>> predicates) {
        Sequence<Predicate<T>> sequence = Sequences.sequence(predicates).unsafeCast();
        if(sequence.size().equals(1)){
            return logicalPredicate(sequence.head());
        }
        return new OrPredicate<T>(sequence);
    }

    public boolean matches(T value) {
        for (Predicate<? super T> predicate : predicates) {
            if (predicate.matches(value)) return true;
        }
        return false;
    }

    public Sequence<Predicate<T>> predicates() {
        return predicates;
    }
}

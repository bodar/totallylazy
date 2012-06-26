package com.googlecode.totallylazy.predicates;

import com.googlecode.totallylazy.Predicate;
import com.googlecode.totallylazy.Predicates;
import com.googlecode.totallylazy.Sequence;
import com.googlecode.totallylazy.Sequences;

import static com.googlecode.totallylazy.Predicates.all;

public class AndPredicate<T> extends LogicalPredicate<T> {
    private final Sequence<Predicate<T>> predicates;

    private AndPredicate(Sequence<Predicate<T>> predicates) {
        this.predicates = predicates;
    }

    public static <T> LogicalPredicate<T> and(Iterable<? extends Predicate<? super T>> predicates){
        Sequence<Predicate<T>> sequence = Sequences.sequence(predicates).unsafeCast();
        if(sequence.size() == 1){
            return logicalPredicate(sequence.head());
        }
        return new AndPredicate<T>(sequence);
    }

    public boolean matches(T value) {
        return predicates.forAll(Predicates.<T>matches(value));
    }

    public Sequence<Predicate<T>> predicates() {
        return predicates;
    }

    @Override
    public int hashCode() {
        return 31 * predicates.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof AndPredicate && predicates.equals(((AndPredicate) obj).predicates());
    }

    @Override
    public String toString() {
        return predicates.toString(" and ");
    }

}

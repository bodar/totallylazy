package com.googlecode.totallylazy.predicates;

import com.googlecode.totallylazy.functions.Function1;
import com.googlecode.totallylazy.Predicate;
import com.googlecode.totallylazy.Predicates;
import com.googlecode.totallylazy.Sequence;
import com.googlecode.totallylazy.Sequences;
import com.googlecode.totallylazy.Unchecked;
import com.googlecode.totallylazy.annotations.multimethod;

import static com.googlecode.totallylazy.Predicates.instanceOf;
import static com.googlecode.totallylazy.Sequences.one;

public class AndPredicate<T> extends LogicalPredicate<T> {
    private final Sequence<Predicate<T>> predicates;

    private AndPredicate(Sequence<Predicate<T>> predicates) {
        this.predicates = predicates;
    }

    public static <T> LogicalPredicate<T> and(Iterable<? extends Predicate<? super T>> predicates) {
        Sequence<Predicate<T>> sequence = Sequences.sequence(predicates).<Predicate<T>>unsafeCast().
                flatMap(AndPredicate.<T>asPredicates());
        if (sequence.exists(instanceOf(AlwaysFalse.class))) return Predicates.alwaysFalse();

        Sequence<Predicate<T>> collapsed = sequence.
                filter(instanceOf(AlwaysTrue.class).not());
        if (collapsed.isEmpty()) return Predicates.alwaysTrue();
        if (collapsed.size() == 1) return logicalPredicate(collapsed.head());
        if (collapsed.forAll(instanceOf(Not.class)))
            return Predicates.not(Predicates.<T>or(sequence.<Not<T>>unsafeCast().map(Not.functions.<T>predicate())));
        return new AndPredicate<T>(collapsed);
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

    @multimethod
    public boolean equals(AndPredicate other) {
        return predicates.equals(other.predicates());
    }

    @Override
    public String toString() {
        return predicates.toString("(", " and ", ")");
    }

    private static <T> Function1<Predicate<T>, Iterable<Predicate<T>>> asPredicates() {
        return predicate -> predicate instanceof AndPredicate ? Unchecked.<AndPredicate<T>>cast(predicate).predicates() : one(predicate);
    }

}

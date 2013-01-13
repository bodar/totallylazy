package com.googlecode.totallylazy.predicates;

import com.googlecode.totallylazy.Mapper;
import com.googlecode.totallylazy.Predicate;
import com.googlecode.totallylazy.Predicates;
import com.googlecode.totallylazy.Sequence;
import com.googlecode.totallylazy.Sequences;
import com.googlecode.totallylazy.Unchecked;

import static com.googlecode.totallylazy.Predicates.instanceOf;
import static com.googlecode.totallylazy.Sequences.one;

public class OrPredicate<T> extends LogicalPredicate<T> {
    private final Sequence<Predicate<T>> predicates;

    private OrPredicate(Sequence<Predicate<T>> predicates) {
        this.predicates = predicates;
    }

    public static <T> LogicalPredicate<T> or(Iterable<? extends Predicate<? super T>> predicates) {
        Sequence<Predicate<T>> sequence = Sequences.sequence(predicates).<Predicate<T>>unsafeCast().
                flatMap(OrPredicate.<T>asPredicates());
        if (sequence.exists(instanceOf(AlwaysTrue.class))) return Predicates.alwaysTrue();

        Sequence<Predicate<T>> collapsed = sequence.
                filter(instanceOf(AlwaysFalse.class).not());
        if (collapsed.isEmpty()) return Predicates.alwaysFalse();
        if (collapsed.size() == 1) return logicalPredicate(collapsed.head());
        if (collapsed.forAll(instanceOf(Not.class)))
            return Predicates.not(Predicates.<T>and(sequence.<Not<T>>unsafeCast().map(Not.functions.<T>predicate())));
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

    @Override
    public int hashCode() {
        return 31 * predicates.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof OrPredicate && predicates.equals(((OrPredicate) obj).predicates());
    }

    @Override
    public String toString() {
        return predicates.toString(" or ");
    }

    private static <T> Mapper<Predicate<T>, Iterable<Predicate<T>>> asPredicates() {
        return new Mapper<Predicate<T>, Iterable<Predicate<T>>>() {
            @Override
            public Iterable<Predicate<T>> call(Predicate<T> predicate) throws Exception {
                return predicate instanceof OrPredicate ? Unchecked.<OrPredicate<T>>cast(predicate).predicates() : one(predicate);
            }
        };
    }
}

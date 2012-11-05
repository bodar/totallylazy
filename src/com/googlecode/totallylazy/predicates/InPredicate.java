package com.googlecode.totallylazy.predicates;

import com.googlecode.totallylazy.Lazy;
import com.googlecode.totallylazy.Unchecked;

import java.util.Collection;

import static com.googlecode.totallylazy.Sequences.sequence;


public class InPredicate<T> extends LogicalPredicate<T> {
    private final Iterable<T> original;
    private final Lazy<Collection<T>> collection;

    private InPredicate(final Iterable<T> iterable) {
        this.collection = new Lazy<Collection<T>>() {
            @Override
            protected Collection<T> get() throws Exception {
                return collection(iterable);
            }
        };
        this.original = iterable;
    }

    public static <T> InPredicate<T> in(final Iterable<? extends T> iterable) {
        return new InPredicate<T>(Unchecked.<Iterable<T>>cast(iterable));
    }

    private Collection<T> collection(Iterable<T> iterable) {
        if(iterable instanceof Collection) return (Collection<T>) iterable;
        return sequence(iterable).toList();
    }

    public boolean matches(T other) {
        return collection.value().contains(other);
    }

    // Used by LazyRecords, do not inline or change
    public Iterable<T> values() {
        return original;
    }
}

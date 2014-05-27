package com.googlecode.totallylazy.matchers;

import com.googlecode.totallylazy.Predicate;
import com.googlecode.totallylazy.Predicates;
import com.googlecode.totallylazy.Sequence;
import com.googlecode.totallylazy.Sequences;
import com.googlecode.totallylazy.predicates.LogicalPredicate;

import java.util.Iterator;

import static com.googlecode.totallylazy.Sequences.sequence;

public class IterablePredicates<T> extends LogicalPredicate<Iterable<T>> {
    private final Sequence<Predicate<? super T>> expected;
    private boolean shouldBeSameSize;

    private IterablePredicates(Iterable<? extends Predicate<? super T>> expected, boolean shouldBeSameSize) {
        this.expected = sequence(expected);
        this.shouldBeSameSize = shouldBeSameSize;
    }

    public static <T> Iterable<Predicate<T>> are(final Iterable<T> values, Class<T> clazz) {
        return are(values);
    }

    public static <T> Iterable<Predicate<T>> are(final Iterable<T> values) {
        return sequence(values).map(v -> Predicates.is(v));
    }

    @SuppressWarnings("unchecked")
    public static Predicate<Iterable> hasSize(final Number size) {
        return new HasSizePredicate(size);
    }

    public static <T> Predicate<Iterable<T>> isEmpty(Class<T> aClass) {
        return IterablePredicates.<T>isEmpty();
    }

    public static <T> Predicate<Iterable<T>> isEmpty() {
        return IterablePredicates.<T>hasExactly(Sequences.<T>empty());
    }

    @SafeVarargs
    public static <T> Predicate<Iterable<T>> hasExactly(final T... items) {
        return hasExactly(sequence(items));
    }

    public static <T> Predicate<Iterable<T>> hasExactly(Iterable<T> expected) {
        return new IterablePredicates<T>(are(expected), true);
    }

    public static <T> Predicate<Iterable<T>> hasExactlyMatching(Iterable<? extends Predicate<? super T>> expected) {
        return new IterablePredicates<T>(expected, true);
    }

    @SafeVarargs
    public static <T> Predicate<Iterable<T>> startsWith(T... items) {
        return startsWith(sequence(items));
    }

    public static <T> Predicate<Iterable<T>> startsWith(Iterable<T> expected) {
        Iterable<? extends Predicate<T>> are = are(expected);
        return new IterablePredicates<T>(are, false);
    }

    @Override
    public boolean matches(Iterable<T> actual) {
        Iterator<Predicate<? super T>> e = this.expected.iterator();
        Iterator<T> a = actual.iterator();
        while(e.hasNext()){
            if(!a.hasNext()){
                return false;
            }

            if(!e.next().matches(a.next())){
                return false;
            }
        }
        if(shouldBeSameSize && a.hasNext()){
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "An iterable with values matching: " + expected;
    }
}

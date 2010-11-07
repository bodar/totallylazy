package com.googlecode.totallylazy;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import static com.googlecode.totallylazy.Callables.size;
import static com.googlecode.totallylazy.Predicates.contains;
import static java.util.Arrays.asList;

public class Sets {
    public static <T> Set<T> set(T... values) {
        Set<T> result = new HashSet<T>();
        result.addAll(asList(values));
        return result;
    }

    public static <T> Set<T> set(final Iterable<? extends T> iterable) {
        return set(iterable.iterator());
    }

    public static <T> Set<T> set(final Iterator<? extends T> iterator) {
        Set<T> result = new HashSet<T>();
        while (iterator.hasNext()) {
            result.add(iterator.next());
        }
        return result;
    }

    public static <T> Set<T> union(final Set<? extends T>... sets) {
        return union(asList(sets));
    }

    public static <T> Set<T> union(final Iterable<Set<? extends T>> sets) {
        Set<T> result = new HashSet<T>();
        for (Set<? extends T> set : sets) {
            result.addAll(set);
        }
        return result;
    }

    public static <T> Set<T> intersection(final Set<? extends T>... sets) {
        return intersection(asList(sets));
    }

    public static <T> Set<T> intersection(final Iterable<Set<? extends T>> iterables) {
        Sequence<Set<? extends T>> sets = Sequences.sequence(iterables).sortBy(size());
        Set<? extends T> smallest = sets.head();
        Sequence<Set<? extends T>> theRest = sets.tail();
        Set<T> result = new HashSet<T>();
        for (T t : smallest) {
            if (theRest.forAll(contains(t))) {
                result.add(t);
            }
        }
        return result;
    }

    public static <T> Set<T> complement(final Set<? extends T>... sets) {
        return complement(asList(sets));
    }

    public static <T> Set<T> complement(final Iterable<Set<? extends T>> iterables) {
        Sequence<Set<? extends T>> sets = Sequences.sequence(iterables);
        Set<? extends T> head = sets.head();
        Sequence<Set<? extends T>> theRest = sets.tail();
        Set<T> result = new HashSet<T>();
        result.addAll(head);
        for (Set<? extends T> set : theRest) {
            result.removeAll(set);
        }
        return result;
    }
}

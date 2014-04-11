package com.googlecode.totallylazy;

import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import static com.googlecode.totallylazy.Callables.size;
import static com.googlecode.totallylazy.Predicates.contains;
import static com.googlecode.totallylazy.Sequences.sequence;
import static java.util.Arrays.asList;
import static java.util.Collections.newSetFromMap;

public class Sets {
    public static <T> Set<T> set() {
        return new LinkedHashSet<T>();
    }

    @SafeVarargs
    public static <T> Set<T> set(T... values) {
        return set(new LinkedHashSet<T>(), values);
    }

    @SafeVarargs
    public static <T, S extends Set<T>> S set(S result, T... values) {
        return set(result, asList(values));
    }

    public static <T> Set<T> set(final Iterable<? extends T> iterable) {
        return set(new LinkedHashSet<T>(), iterable);
    }

    public static <T, S extends Set<T>> S set(S result, final Iterable<? extends T> iterable) {
        return set(result, iterable.iterator());
    }

    public static <T> Set<T> set(final Iterator<? extends T> iterator) {
        return set(new LinkedHashSet<T>(), iterator);
    }

    public static <T, S extends Set<T>> S set(S result, final Iterator<? extends T> iterator) {
        while (iterator.hasNext()) {
            result.add(iterator.next());
        }
        return result;
    }

    public static <T> Set<T> fifoSet(final int maximumElements) {
        return newSetFromMap(Maps.<T, Boolean>fifoMap(maximumElements));
    }

    @SafeVarargs
    public static <T> Set<T> union(final Set<? extends T>... sets) {
        return union(asList(sets));
    }

    public static <T> Set<T> union(final Iterable<? extends Set<? extends T>> sets) {
        Set<T> result = new LinkedHashSet<T>();
        for (Set<? extends T> set : sets) {
            result.addAll(set);
        }
        return result;
    }

    @SafeVarargs
    public static <T> Set<T> intersection(final Set<? extends T>... sets) {
        return intersection(sequence(sets));
    }

    public static <T> Set<T> intersection(final Iterable<? extends Set<? extends T>> iterables) {
        Seq<Set<T>> sets = sequence(iterables).sortBy(size()).unsafeCast();
        Set<T> smallest = sets.head();
        Seq<Set<T>> theRest = sets.tail();
        Set<T> result = new LinkedHashSet<T>();
        for (T t : smallest) {
            if (theRest.forAll(contains(t))) {
                result.add(t);
            }
        }
        return result;
    }

    @SafeVarargs
    public static <T> Set<T> complement(final Set<? extends T>... sets) {
        return complement(asList(sets));
    }

    public static <T> Set<T> complement(final Iterable<? extends Set<? extends T>> iterables) {
        Seq<Set<T>> sets = sequence(iterables).unsafeCast();
        Set<T> head = sets.head();
        Seq<Set<T>> theRest = sets.tail();
        Set<T> result = new LinkedHashSet<T>();
        result.addAll(head);
        for (Set<T> set : theRest) {
            result.removeAll(set);
        }
        return result;
    }

    public static <T> Set<T> concurrentSet() {
        return newSetFromMap(new ConcurrentHashMap<T, Boolean>());
    }
}

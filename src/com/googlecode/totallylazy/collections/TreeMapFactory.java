package com.googlecode.totallylazy.collections;

import com.googlecode.totallylazy.Callables;
import com.googlecode.totallylazy.Pair;
import com.googlecode.totallylazy.comparators.Comparators;

import java.util.Comparator;

import static com.googlecode.totallylazy.Sequences.sequence;
import static com.googlecode.totallylazy.Unchecked.cast;

public class TreeMapFactory<K, V, T extends TreeMap<K, V>> extends AbstractMapFactory<K, V, T> {
    private final TreeFactory factory;
    private final Comparator<K> comparator;

    protected TreeMapFactory(TreeFactory factory, Comparator<K> comparator) {
        this.factory = factory;
        this.comparator = comparator;
    }

    public static <K, V, T extends TreeMap<K, V>> TreeMapFactory<K, V, T> treeMapFactory(TreeFactory factory, Comparator<K> comparator) {
        return new TreeMapFactory<K, V, T>(factory, comparator);
    }

    public static <K extends Comparable<? super K>, V, T extends TreeMap<K, V>> TreeMapFactory<K, V, T> treeMapFactory(TreeFactory factory) {
        return new TreeMapFactory<K, V, T>(factory, Comparators.<K>ascending());
    }

    @Override
    public T empty() {
        return cast(factory.create(comparator));
    }

    @Override
    public T map(Iterable<? extends Pair<K, V>> values) {
        return cast(TreeMap.methods.treeMap(factory, comparator, sequence(values).toSortedList(Comparators.<Pair<K, V>, K>by(Callables.<K>first(), comparator))));
    }
}

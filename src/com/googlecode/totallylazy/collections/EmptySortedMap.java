package com.googlecode.totallylazy.collections;

import com.googlecode.totallylazy.Callable2;

public class EmptySortedMap<K, V> extends AbstractEmptyTreeMap<K, V, ImmutableSortedMap<K, V>> {
    public EmptySortedMap(Callable2<? super K, ? super V, ? extends ImmutableSortedMap<K, V>> creator) {
        super(creator);
    }

    public static <K, V> EmptySortedMap<K, V> emptyMap(Callable2<? super K, ? super V, ? extends ImmutableSortedMap<K, V>> creator) {
        return new EmptySortedMap<K, V>(creator);
    }

    @Override
    protected EmptySortedMap<K, V> self() {
        return this;
    }
}
package com.googlecode.totallylazy.collections;

import com.googlecode.totallylazy.Callables;
import com.googlecode.totallylazy.Eq;
import com.googlecode.totallylazy.Option;
import com.googlecode.totallylazy.Pair;
import com.googlecode.totallylazy.Predicate;
import com.googlecode.totallylazy.Predicates;
import com.googlecode.totallylazy.Segment;
import com.googlecode.totallylazy.Sequence;
import com.googlecode.totallylazy.Sequences;

import java.util.Map;
import java.util.concurrent.ConcurrentMap;

public abstract class AbstractMap<K, V> extends Eq implements PersistentMap<K, V> {
    @Override
    public Map<K, V> toMap() {
        return methods.toMap(this);
    }

    @Override
    public ConcurrentMap<K, V> toConcurrentMap() {
        return AtomicMap.atomicMap(this);
    }

    @Override
    public Sequence<Pair<K, V>> toSequence() {
        return Sequences.sequence(this);
    }

    @Override
    public Sequence<K> keys() {
        return toSequence().map(Callables.<K>first());
    }

    @Override
    public Sequence<V> values() {
        return toSequence().map(Callables.<V>second());
    }

    @Override
    public PersistentList<Pair<K, V>> toPersistentList() {
        return toSequence().toPersistentList();
    }

    @Override
    public Option<V> find(Predicate<? super K> predicate) {
        return toSequence().find(Predicates.<K>first(predicate)).map(Callables.<V>second());
    }

    @Override
    public <C extends Segment<Pair<K, V>>> C joinTo(C rest) {
        return toSequence().joinTo(rest);
    }
}

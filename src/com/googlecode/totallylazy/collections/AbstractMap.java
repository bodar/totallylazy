package com.googlecode.totallylazy.collections;

import com.googlecode.totallylazy.Callables;
import com.googlecode.totallylazy.Option;
import com.googlecode.totallylazy.Pair;
import com.googlecode.totallylazy.Predicate;
import com.googlecode.totallylazy.Predicates;
import com.googlecode.totallylazy.Segment;
import com.googlecode.totallylazy.Sequence;
import com.googlecode.totallylazy.Sequences;
import com.googlecode.totallylazy.Unchecked;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;

@SuppressWarnings("deprecation")
public abstract class AbstractMap<K, V> extends ReadOnlyMap<K,V> implements PersistentMap<K, V> {
    @Override
    public Map<K, V> toMutableMap() {
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


    @Override
    public Set<K> keySet() {
        return keys().toSet();
    }

    @Override
    public boolean containsKey(Object key) {
        return contains(key);
    }

    @Override
    public Set<Entry<K, V>> entrySet() {
        return Sequences.<Entry<K, V>>sequence(this).toSet();
    }

    @Override
    public V get(Object key) {
        return lookup(Unchecked.<K>cast(key)).getOrNull();
    }

    @Override
    public boolean containsValue(Object value) {
        return values().contains(value);
    }
}

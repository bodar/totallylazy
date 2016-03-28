package com.googlecode.totallylazy.collections;

import com.googlecode.totallylazy.functions.Function1;
import com.googlecode.totallylazy.functions.Function2;
import com.googlecode.totallylazy.functions.Callables;
import com.googlecode.totallylazy.Option;
import com.googlecode.totallylazy.Pair;
import com.googlecode.totallylazy.predicates.Predicate;
import com.googlecode.totallylazy.predicates.Predicates;
import com.googlecode.totallylazy.Sequences;
import com.googlecode.totallylazy.annotations.multimethod;

import java.util.Iterator;
import java.util.NoSuchElementException;

import static com.googlecode.totallylazy.Sequences.sequence;

public class HashTreeMap<K, V> extends AbstractMap<K, V> {
    private final PersistentSortedMap<Integer, PersistentMap<K, V>> hash;
    private final PersistentMap<K, V> emptyBucket = ListMap.<K, V>emptyListMap();

    private HashTreeMap(PersistentSortedMap<Integer, PersistentMap<K, V>> hash) {
        this.hash = hash;
    }

    public static <K, V> HashTreeMapFactory<K, V> factory() {
        return HashTreeMapFactory.factory();
    }

    public static <K, V> HashTreeMap<K, V> hashTreeMap(PersistentSortedMap<Integer, PersistentMap<K, V>> map) {
        return new HashTreeMap<K, V>(map);
    }

    public static <K, V> HashTreeMap<K, V> hashTreeMap() {
        return hashTreeMap(PersistentSortedMap.constructors.<Integer, PersistentMap<K, V>>sortedMap());
    }

    public static <K, V> HashTreeMap<K, V> hashTreeMap(Iterable<? extends Pair<K, V>> values) {
        return HashTreeMap.<K,V>factory().map(values);
    }

    @Override
    public PersistentMap<K, V> empty() {
        return hashTreeMap();
    }

    @Override
    public boolean isEmpty() {
        return hash.isEmpty();
    }

    @Override
    public Pair<K, V> head() throws NoSuchElementException {
        return iterator().next();
    }

    @Override
    public Option<Pair<K, V>> headOption() {
        if(isEmpty()) return Option.none();
        return Option.some(head());
    }

    @Override
    public PersistentMap<K, V> cons(Pair<K, V> head) {
        return insert(head.first(), head.second());
    }

    @Override
    public PersistentMap<K, V> tail() throws NoSuchElementException {
        return delete(head().first());
    }

    @Override
    public Option<V> lookup(final K key) {
        return hash.lookup(key.hashCode()).flatMap(PersistentMap.functions.<K, V>get(key));
    }

    @Override
    public PersistentMap<K, V> insert(K key, V value) {
        int hashCode = key.hashCode();
        return hashTreeMap(hash.insert(hashCode, hash.lookup(hashCode).getOrElse(emptyBucket).insert(key, value)));
    }

    @Override
    public PersistentMap<K, V> delete(K key) {
        int hashCode = key.hashCode();
        PersistentMap<K, V> bucket = hash.lookup(hashCode).getOrElse(emptyBucket).delete(key);
        if(bucket.isEmpty()) return hashTreeMap(hash.delete(hashCode));
        return hashTreeMap(hash.insert(hashCode, bucket));
    }

    @Override
    public <S> S fold(S seed, Function2<? super S, ? super Pair<K, V>, ? extends S> callable) {
        return toSequence().fold(seed, callable);
    }

    @Override
    public Iterator<Pair<K, V>> iterator() {
        return hash.values().flatMap(Sequences.<Pair<K, V>>identity()).iterator();
    }

    @Override
    public boolean contains(final Object other) {
        return hash.lookup(other.hashCode()).map(PersistentMap.functions.<K, V>contains(other)).getOrElse(false);
    }

    @Override
    public boolean exists(Predicate<? super K> predicate) {
        return toSequence().exists(Predicates.<K>first(predicate));
    }

    @Override
    public int size() {
        return toSequence().size();
    }

    @Override
    public int hashCode() {
        return toSequence().hashCode();
    }

    @multimethod
    public boolean equals(HashTreeMap<K, V> obj) {
        return toSequence().equals(obj.toSequence());
    }

    @Override
    public String toString() {
        return toSequence().toString("");
    }
}

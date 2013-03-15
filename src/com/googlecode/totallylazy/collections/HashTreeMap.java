package com.googlecode.totallylazy.collections;

import com.googlecode.totallylazy.Callable1;
import com.googlecode.totallylazy.Callable2;
import com.googlecode.totallylazy.Callables;
import com.googlecode.totallylazy.Option;
import com.googlecode.totallylazy.Pair;
import com.googlecode.totallylazy.Predicate;
import com.googlecode.totallylazy.Predicates;
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
        return put(head.first(), head.second());
    }

    @Override
    public PersistentMap<K, V> tail() throws NoSuchElementException {
        return remove(head().first());
    }

    @Override
    public Option<V> get(final K key) {
        return hash.get(key.hashCode()).flatMap(PersistentMap.functions.<K, V>get(key));
    }

    @Override
    public PersistentMap<K, V> put(K key, V value) {
        int hashCode = key.hashCode();
        return hashTreeMap(hash.put(hashCode, hash.get(hashCode).getOrElse(emptyBucket).put(key, value)));
    }

    @Override
    public PersistentMap<K, V> remove(K key) {
        int hashCode = key.hashCode();
        PersistentMap<K, V> bucket = hash.get(hashCode).getOrElse(emptyBucket).remove(key);
        if(bucket.isEmpty()) return hashTreeMap(hash.remove(hashCode));
        return hashTreeMap(hash.put(hashCode, bucket));
    }

    @Override
    public PersistentMap<K, V> filterKeys(Predicate<? super K> predicate) {
        return hashTreeMap(toSequence().filter(Predicates.<K>first(predicate)));
    }

    @Override
    public PersistentMap<K, V> filterValues(Predicate<? super V> predicate) {
        return hashTreeMap(toSequence().filter(Predicates.<V>second(predicate)));
    }

    @Override
    public <NewV> PersistentMap<K, NewV> map(Callable1<? super V, ? extends NewV> transformer) {
        return hashTreeMap(toSequence().map(Callables.<K, V, NewV>second(transformer)));
    }

    @Override
    public <S> S fold(S seed, Callable2<? super S, ? super Pair<K, V>, ? extends S> callable) {
        return toSequence().fold(seed, callable);
    }

    @Override
    public Iterator<Pair<K, V>> iterator() {
        return hash.values().flatMap(Sequences.<Pair<K, V>>identity()).iterator();
    }

    @Override
    public boolean contains(final K other) {
        return hash.get(other.hashCode()).map(PersistentMap.functions.<K, V>contains(other)).getOrElse(false);
    }

    @Override
    public boolean exists(Predicate<? super K> predicate) {
        throw new UnsupportedOperationException();
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

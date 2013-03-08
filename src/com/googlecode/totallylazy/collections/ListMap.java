package com.googlecode.totallylazy.collections;

import com.googlecode.totallylazy.Callable1;
import com.googlecode.totallylazy.Callable2;
import com.googlecode.totallylazy.Callables;
import com.googlecode.totallylazy.First;
import com.googlecode.totallylazy.Option;
import com.googlecode.totallylazy.Pair;
import com.googlecode.totallylazy.Predicate;
import com.googlecode.totallylazy.Predicates;
import com.googlecode.totallylazy.Segment;
import com.googlecode.totallylazy.Sequences;

import java.util.Iterator;
import java.util.NoSuchElementException;

import static com.googlecode.totallylazy.Pair.pair;
import static com.googlecode.totallylazy.Predicates.is;
import static com.googlecode.totallylazy.Predicates.not;
import static com.googlecode.totallylazy.Predicates.where;
import static com.googlecode.totallylazy.collections.PersistentList.constructors.list;
import static com.googlecode.totallylazy.collections.PersistentList.constructors.reverse;

public class ListMap<K, V> extends AbstractMap<K, V> {
    private PersistentList<Pair<K, V>> list;

    private ListMap(PersistentList<Pair<K, V>> list) {
        this.list = list;
    }

    public static <K, V> ListMapFactory<K, V> factory() {
        return new ListMapFactory<K, V>();
    }

    public static <K, V> PersistentMap<K, V> emptyListMap(Class<K> kClass, Class<V> vClass) {
        return emptyListMap();
    }

    public static <K, V> PersistentMap<K, V> emptyListMap() {
        return listMap(PersistentList.constructors.<Pair<K, V>>empty());
    }

    public static <K, V> PersistentMap<K, V> listMap(K key, V value) {
        return listMap(pair(key, value));
    }

    public static <K, V> PersistentMap<K, V> listMap(K key1, V value1, K key2, V value2) {
        return listMap(Sequences.sequence(pair(key1, value1), pair(key2, value2)));
    }

    public static <K, V> PersistentMap<K, V> listMap(K key1, V value1, K key2, V value2, K key3, V value3) {
        return listMap(Sequences.sequence(pair(key1, value1), pair(key2, value2), pair(key3, value3)));
    }

    public static <K, V> PersistentMap<K, V> listMap(K key1, V value1, K key2, V value2, K key3, V value3, K key4, V value4) {
        return listMap(Sequences.sequence(pair(key1, value1), pair(key2, value2), pair(key3, value3), pair(key4, value4)));
    }

    public static <K, V> PersistentMap<K, V> listMap(K key1, V value1, K key2, V value2, K key3, V value3, K key4, V value4, K key5, V value5) {
        return listMap(Sequences.sequence(pair(key1, value1), pair(key2, value2), pair(key3, value3), pair(key4, value4), pair(key5, value5)));
    }

    public static <K, V> PersistentMap<K, V> listMap(Iterable<? extends Pair<K, V>> pairs) {
        return listMap(reverse(Sequences.sequence((pairs))));
    }

    public static <K, V> PersistentMap<K, V> listMap(Pair<K, V> pair) {
        return listMap(list(pair));
    }

    public static <K, V> PersistentMap<K, V> listMap(PersistentList<Pair<K, V>> list1) {
        return new ListMap<K, V>(list1);
    }

    @Override
    public PersistentMap<K, V> empty() {
        return emptyListMap();
    }

    @Override
    public PersistentMap<K, V> cons(Pair<K, V> head) {
        return contains(head.first()) ? listMap(list.map(replace(head))) : listMap(list.cons(head));
    }

    private Callable1<Pair<K, V>, Pair<K, V>> replace(final Pair<K, V> newValue) {
        return new Callable1<Pair<K, V>, Pair<K, V>>() {
            @Override
            public Pair<K, V> call(Pair<K, V> oldValue) throws Exception {
                return oldValue.first().equals(newValue.first()) ? newValue : oldValue;
            }
        };
    }

    @Override
    public <C extends Segment<Pair<K, V>>> C joinTo(C rest) {
        return list.joinTo(rest);
    }

    @Override
    public boolean isEmpty() {
        return list.isEmpty();
    }

    @Override
    public Pair<K, V> head() throws NoSuchElementException {
        return list.head();
    }

    @Override
    public Option<Pair<K, V>> headOption() {
        return list.headOption();
    }

    @Override
    public PersistentMap<K, V> tail() throws NoSuchElementException {
        return listMap(list.tail());
    }

    @Override
    public Option<V> get(K key) {
        return find(is(key));
    }

    @Override
    public Option<V> find(Predicate<? super K> predicate) {
        return list.find(key(predicate)).map(Callables.<V>second());
    }

    @Override
    public PersistentMap<K, V> put(K key, V value) {
        return cons(Pair.pair(key, value));
    }

    @Override
    public PersistentMap<K, V> remove(K key) {
        return filterKeys(is(not(key)));
    }

    @Override
    public PersistentMap<K, V> filterKeys(Predicate<? super K> predicate) {
        return listMap(list.filter(key(predicate)));
    }

    @Override
    public PersistentMap<K, V> filterValues(Predicate<? super V> predicate) {
        return listMap(list.filter(where(Callables.<V>second(), predicate)));
    }

    @Override
    public <NewV> PersistentMap<K, NewV> map(Callable1<? super V, ? extends NewV> transformer) {
        return listMap(list.map(Callables.<K, V, NewV>second(transformer)));
    }

    @Override
    public int size() {
        return list.size();
    }

    @Override
    public PersistentList<Pair<K, V>> toPersistentList() {
        return reverse(list);
    }

    @Override
    public boolean contains(K other) {
        return list.exists(key(other));
    }

    @Override
    public boolean exists(Predicate<? super K> predicate) {
        return list.exists(key(predicate));
    }

    @Override
    public Iterator<Pair<K, V>> iterator() {
        return toPersistentList().iterator();
    }

    private Predicate<First<K>> key(Predicate<? super K> predicate) {
        return Predicates.first(predicate);
    }

    private Predicate<First<K>> key(K key) {
        return key(is(key));
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof ListMap && ((ListMap) obj).list.equals(list);
    }

    @Override
    public int hashCode() {
        return list.hashCode();
    }

    @Override
    public String toString() {
        return toPersistentList().toString();
    }

    @Override
    public <S> S fold(S seed, Callable2<? super S, ? super Pair<K, V>, ? extends S> callable) {
        return list.fold(seed, callable);
    }
}

package com.googlecode.totallylazy.collections;

import com.googlecode.totallylazy.functions.Function1;
import com.googlecode.totallylazy.functions.Function2;
import com.googlecode.totallylazy.functions.Callables;
import com.googlecode.totallylazy.First;
import com.googlecode.totallylazy.Option;
import com.googlecode.totallylazy.Pair;
import com.googlecode.totallylazy.predicates.Predicate;
import com.googlecode.totallylazy.predicates.Predicates;
import com.googlecode.totallylazy.Segment;
import com.googlecode.totallylazy.Sequences;

import java.util.Iterator;
import java.util.NoSuchElementException;

import static com.googlecode.totallylazy.Pair.pair;
import static com.googlecode.totallylazy.Unchecked.cast;
import static com.googlecode.totallylazy.predicates.Predicates.is;
import static com.googlecode.totallylazy.predicates.Predicates.not;
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
        return contains(head.first()) ? listMap(map(list, replace(head))) : listMap(list.cons(head));
    }

    private static <A,B> PersistentList<B> map(PersistentList<A> list, Function1<A, B> mapper) {
        if(list.isEmpty()) return cast(list);
        return map(list.tail(), mapper).cons(mapper.apply(list.head()));
    }

    private Function1<Pair<K, V>, Pair<K, V>> replace(final Pair<K, V> newValue) {
        return oldValue -> oldValue.first().equals(newValue.first()) ? newValue : oldValue;
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
    public Option<V> lookup(K key) {
        return list.toSequence().find(key(key)).map(Pair::getValue);
    }

    @Override
    public PersistentMap<K, V> insert(K key, V value) {
        return cons(Pair.pair(key, value));
    }

    @Override
    public PersistentMap<K, V> delete(K key) {
        return listMap(toSequence().reject(p -> p.getKey().equals(key)));
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
    public boolean contains(Object other) {
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

    private Predicate<First<K>> key(Object key) {
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
    public <S> S fold(S seed, Function2<? super S, ? super Pair<K, V>, ? extends S> callable) {
        return list.toSequence().fold(seed, callable);
    }
}

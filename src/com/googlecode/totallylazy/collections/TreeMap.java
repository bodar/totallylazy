package com.googlecode.totallylazy.collections;

import com.googlecode.totallylazy.Callable1;
import com.googlecode.totallylazy.Callers;
import com.googlecode.totallylazy.Function2;
import com.googlecode.totallylazy.Option;
import com.googlecode.totallylazy.Pair;
import com.googlecode.totallylazy.Predicate;
import com.googlecode.totallylazy.Segment;
import com.googlecode.totallylazy.comparators.Comparators;
import com.googlecode.totallylazy.iterators.SegmentIterator;

import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;

import static com.googlecode.totallylazy.Pair.pair;

public class TreeMap<K, V> implements ImmutableMap<K, V> {
    protected final ImmutableMap<K, V> left;
    protected final K key;
    protected final V value;
    protected final ImmutableMap<K, V> right;
    protected final Comparator<K> comparator;

    TreeMap(ImmutableMap<K, V> left, K key, V value, ImmutableMap<K, V> right, Comparator<K> comparator) {
        this.left = left;
        this.key = key;
        this.value = value;
        this.right = right;
        this.comparator = comparator;
    }

    <K,V> TreeMap<K, V> create(ImmutableMap<K, V> left, K key, V value, ImmutableMap<K, V> right, Comparator<K> comparator) {
        return tree(left, key, value, right, comparator);
    }

    static <K extends Comparable<? super K>, V> TreeMap<K, V> tree(K key, V value) {
        return tree(TreeMap.<K,V>empty(), key, value, TreeMap.<K,V>empty());
    }

    static <K extends Comparable<? super K>, V> EmptyMap<K, V> empty() {
        return EmptyMap.<K, V>emptyMap(TreeMap.<K, V>tree());
    }

    private static <K extends Comparable<? super K>, V> Function2<K, V, ImmutableMap<K, V>> tree() {
        return new Function2<K, V, ImmutableMap<K, V>>() {
            @Override
            public ImmutableMap<K, V> call(K k, V v) throws Exception {
                return tree(k, v);
            }
        };
    }

    static <K extends Comparable<? super K>, V> TreeMap<K, V> tree(ImmutableMap<K, V> left, Pair<K, V> pair, ImmutableMap<K, V> right) {
        return TreeMap.<K, V>tree(left, pair.first(), pair.second(), right, Comparators.<K>ascending());
    }

    static <K extends Comparable<? super K>, V> TreeMap<K, V> tree(ImmutableMap<K, V> left, K key, V value, ImmutableMap<K, V> right) {
        return new TreeMap<K, V>(left, key, value, right, Comparators.<K>ascending());
    }

    static <K, V> TreeMap<K, V> tree(ImmutableMap<K, V> left, K key, V value, ImmutableMap<K, V> right, Comparator<K> comparator) {
        return new TreeMap<K, V>(left, key, value, right, comparator);
    }

    static <K, V> TreeMap<K, V> tree(ImmutableMap<K, V> left, Pair<K, V> pair, ImmutableMap<K, V> right, Comparator<K> comparator) {
        return new TreeMap<K, V>(left, pair.first(), pair.second(), right, comparator);
    }

    static <K, V> TreeMap<K, V> tree(K key, V value, Comparator<K> comparator) {
        return tree(TreeMap.<K, V>empty(comparator), key, value, TreeMap.<K, V>empty(comparator), comparator);
    }

    static <K,V> ImmutableMap<K, V> empty(Comparator<K> comparator) {
        return EmptyMap.<K, V>emptyMap(TreeMap.<K, V>tree(comparator));
    }

    private static <K, V> Function2<K, V, ImmutableMap<K, V>> tree(final Comparator<K> comparator) {
        return new Function2<K, V, ImmutableMap<K, V>>() {
            @Override
            public ImmutableMap<K, V> call(K k, V v) throws Exception {
                return tree(k, v, comparator);
            }
        };
    }

    @Override
    public ImmutableList<Pair<K, V>> immutableList() {
        return joinTo(ImmutableList.constructors.<Pair<K, V>>empty());
    }

    @Override
    public Option<V> get(K other) {
        int difference = comparator.compare(other, key);
        if (difference == 0) return Option.option(value);
        if (difference < 0) return left.get(other);
        return right.get(other);
    }

    @Override
    public ImmutableMap<K, V> put(K key, V value) {
        return cons(pair(key, value));
    }

    @Override
    public Option<V> find(Predicate<? super K> predicate) {
        if(predicate.matches(key)) return Option.some(value);
        Option<V> left = this.left.find(predicate);
        if(left.isEmpty()) return right.find(predicate);
        return left;
    }

    @Override
    public ImmutableMap<K, V> filterKeys(Predicate<? super K> predicate) {
        if(predicate.matches(key)) return create(left.filterKeys(predicate), key, value, right.filterKeys(predicate), comparator);
        return left.filterKeys(predicate).joinTo(right.filterKeys(predicate));
    }

    @Override
    public ImmutableMap<K, V> filterValues(Predicate<? super V> predicate) {
        if(predicate.matches(value)) return create(left.filterValues(predicate), key, value, right.filterValues(predicate), comparator);
        return left.filterValues(predicate).joinTo(right.filterValues(predicate));
    }

    @Override
    public <NewV> ImmutableMap<K, NewV> mapValues(Callable1<? super V, ? extends NewV> transformer) {
        return create(left.mapValues(transformer), key, Callers.call(transformer, value), right.mapValues(transformer), comparator);
    }

    @Override
    public ImmutableMap<K, V> remove(K key) {
        if(comparator.compare(this.key, key) == 0) return left.joinTo(right);
        return create(left.remove(key), this.key, value, right.remove(key), comparator);
    }

    @Override
    public Pair<ImmutableMap<K, V>, Pair<K,V>>  removeMinimum() {
        if(left.isEmpty()) return pair(right, pair(key, value));
        final Pair<ImmutableMap<K, V>, Pair<K, V>> newLeft = left.removeMinimum();
        return Pair.<ImmutableMap<K, V>, Pair<K, V>>pair(create(newLeft.first(), key, value, right, comparator), newLeft.second());
    }

    @Override
    public Pair<ImmutableMap<K, V>, Pair<K,V>> removeMaximum() {
        if(right.isEmpty()) return pair(left, pair(key, value));;
        final Pair<ImmutableMap<K, V>, Pair<K, V>> newRight = right.removeMaximum();
        return Pair.<ImmutableMap<K, V>, Pair<K, V>>pair(create(left, key, value, newRight.first(), comparator), newRight.second());
    }

    @Override
    public <C extends Segment<Pair<K, V>, C>> C joinTo(C rest) {
        return left.joinTo(right.joinTo(rest).cons(pair(key, value)));
    }

    @Override
    public ImmutableMap<K, V> cons(Pair<K, V> newValue) {
        int difference = comparator.compare(newValue.first(), key);
        if (difference == 0) return create(left, newValue.first(), newValue.second(), right, comparator);
        if (difference < 0) return create(left.cons(newValue), key, value, right, comparator);
        return create(left, key, value, right.cons(newValue), comparator);
    }

    @Override
    public boolean contains(K other) {
        int difference = comparator.compare(other, key);
        if (difference == 0) return key.equals(other);
        if (difference < 0) return left.contains(other);
        return right.contains(other);
    }

    @Override
    public int hashCode() {
        return 19 * value.hashCode() * left.hashCode() * right.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof TreeMap && value.equals(((TreeMap) obj).value) && left.equals(((TreeMap) obj).left) && right.equals(((TreeMap) obj).right);
    }

    @Override
    public String toString() {
        return String.format("(%s %s %s)", left, key, right);
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public Pair<K, V> head() throws NoSuchElementException {
        return pair(key, value);
    }

    @Override
    public ImmutableMap<K, V> tail() throws NoSuchElementException {
        return left.joinTo(right);
    }

    @Override
    public Iterator<Pair<K, V>> iterator() {
        return new SegmentIterator<Pair<K, V>, ImmutableList<Pair<K, V>>>(immutableList());
    }
}

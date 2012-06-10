package com.googlecode.totallylazy.collections;

import com.googlecode.totallylazy.Callable1;
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

import static com.googlecode.totallylazy.Callers.call;
import static com.googlecode.totallylazy.Unchecked.cast;

public class TreeMap<K, V> implements ImmutableSortedMap<K, V> {
    private final ImmutableSortedMap<K, V> left;
    protected final K key;
    protected final V value;
    private final ImmutableSortedMap<K, V> right;
    protected final Comparator<K> comparator;
    protected final int size;

    TreeMap(ImmutableSortedMap<K, V> left, K key, V value, ImmutableSortedMap<K, V> right, Comparator<K> comparator) {
        this.left = left;
        this.key = key;
        this.value = value;
        this.right = right;
        this.comparator = comparator;
        size = left.size() + right.size() + 1;
    }

    public ImmutableSortedMap<K, V> left() {
        return left;
    }

    public ImmutableSortedMap<K, V> right() {
        return right;
    }

    <K, V> TreeMap<K, V> create(ImmutableSortedMap<K, V> left, K key, V value, ImmutableSortedMap<K, V> right, Comparator<K> comparator) {
        return tree(left, key, value, right, comparator);
    }

    static <K extends Comparable<? super K>, V> TreeMap<K, V> tree(K key, V value) {
        return tree(TreeMap.<K, V>empty(), key, value, TreeMap.<K, V>empty());
    }

    static <K extends Comparable<? super K>, V> EmptySortedMap<K, V> empty() {
        return EmptySortedMap.<K, V>emptyMap(TreeMap.<K, V>tree());
    }

    private static <K extends Comparable<? super K>, V> Function2<K, V, TreeMap<K, V>> tree() {
        return new Function2<K, V, TreeMap<K, V>>() {
            @Override
            public TreeMap<K, V> call(K k, V v) throws Exception {
                return tree(k, v);
            }
        };
    }

    static <K extends Comparable<? super K>, V> TreeMap<K, V> tree(ImmutableSortedMap<K, V> left, K key, V value, ImmutableSortedMap<K, V> right) {
        return new TreeMap<K, V>(left, key, value, right, Comparators.<K>ascending());
    }

    static <K, V> TreeMap<K, V> tree(ImmutableSortedMap<K, V> left, K key, V value, ImmutableSortedMap<K, V> right, Comparator<K> comparator) {
        return new TreeMap<K, V>(left, key, value, right, comparator);
    }

    static <K, V> TreeMap<K, V> tree(Comparator<K> comparator, K key, V value) {
        return tree(TreeMap.<K, V>empty(comparator), key, value, TreeMap.<K, V>empty(comparator), comparator);
    }

    static <K, V> ImmutableSortedMap<K, V> empty(Comparator<K> comparator) {
        return EmptySortedMap.<K, V>emptyMap(TreeMap.<K, V>tree(comparator));
    }

    private static <K, V> Function2<K, V, TreeMap<K, V>> tree(final Comparator<K> comparator) {
        return new Function2<K, V, TreeMap<K, V>>() {
            @Override
            public TreeMap<K, V> call(K k, V v) throws Exception {
                return tree(comparator, k, v);
            }
        };
    }

    @Override
    public ImmutableList<Pair<K, V>> immutableList() {
        return joinTo(ImmutableList.constructors.<Pair<K, V>>empty());
    }

    @Override
    public Option<V> get(K other) {
        int difference = difference(other);
        if (difference == 0) return Option.option(value);
        if (difference < 0) return left.get(other);
        return right.get(other);
    }

    @Override
    public ImmutableSortedMap<K, V> put(K key, V value) {
        return cons(Pair.pair(key, value));
    }

    @Override
    public Option<V> find(Predicate<? super K> predicate) {
        if (predicate.matches(key)) return Option.some(value);
        Option<V> left = this.left.find(predicate);
        if (left.isEmpty()) return right.find(predicate);
        return left;
    }

    @Override
    public ImmutableSortedMap<K, V> filterKeys(Predicate<? super K> predicate) {
        if (predicate.matches(key))
            return create(left.filterKeys(predicate), key, value, right.filterKeys(predicate), comparator);
        return left.filterKeys(predicate).joinTo(right.filterKeys(predicate));
    }

    @Override
    public ImmutableSortedMap<K, V> filterValues(Predicate<? super V> predicate) {
        if (predicate.matches(value))
            return create(left.filterValues(predicate), key, value, right.filterValues(predicate), comparator);
        return left.filterValues(predicate).joinTo(right.filterValues(predicate));
    }

    @Override
    public <NewV> ImmutableSortedMap<K, NewV> mapValues(Callable1<? super V, ? extends NewV> transformer) {
        return create(left.mapValues(transformer), key, call(transformer, value), right.mapValues(transformer), comparator);
    }

    @Override
    public ImmutableSortedMap<K, V> remove(K key) {
        int difference = difference(key);
        if (difference == 0) {
            if (left.isEmpty()) return right;
            Pair<? extends ImmutableSortedMap<K, V>, Pair<K, V>> pair = left.removeLast();
            ImmutableSortedMap<K, V> newLeft = pair.first();
            Pair<K, V> newRoot = pair.second();
            return create(newLeft, newRoot.first(), newRoot.second(), right, comparator);
        }
        if (difference < 0) return create(left.remove(key), this.key, value, right, comparator);
        return create(left, this.key, value, right.remove(key), comparator);
    }

    private int difference(K key) {
        return comparator.compare(key, this.key);
    }

    @Override
    public Pair<K, V> first() throws NoSuchElementException {
        if (left.isEmpty()) return pair();
        return left.first();
    }

    @Override
    public Pair<K, V> last() throws NoSuchElementException {
        if (right.isEmpty()) return pair();
        return right.last();
    }

    @Override
    public Pair<ImmutableSortedMap<K, V>, Pair<K, V>> removeFirst() {
        if (left.isEmpty()) return Pair.pair(right, pair());
        final Pair<? extends ImmutableSortedMap<K, V>, Pair<K, V>> newLeft = left.removeFirst();
        return Pair.<ImmutableSortedMap<K, V>, Pair<K, V>>pair(create(newLeft.first(), key, value, right, comparator), newLeft.second());
    }

    @Override
    public Pair<ImmutableSortedMap<K, V>, Pair<K, V>> removeLast() {
        if (right.isEmpty()) return Pair.pair(left, pair());
        final Pair<? extends ImmutableSortedMap<K, V>, Pair<K, V>> newRight = right.removeLast();
        return Pair.<ImmutableSortedMap<K, V>, Pair<K, V>>pair(create(left, key, value, newRight.first(), comparator), newRight.second());
    }

    @Override
    public <C extends Segment<Pair<K, V>>> C joinTo(C rest) {
        return cast(left.joinTo(right.joinTo(rest).cons(pair())));
    }

    @Override
    public ImmutableSortedMap<K, V> cons(Pair<K, V> newValue) {
        int difference = difference(newValue.first());
        if (difference == 0) return create(left, newValue.first(), newValue.second(), right, comparator);
        if (difference < 0) return create(left.cons(newValue), key, value, right, comparator);
        return create(left, key, value, right.cons(newValue), comparator);
    }

    @Override
    public boolean contains(K other) {
        int difference = difference(other);
        if (difference == 0) return true;
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
        return pair();
    }

    @Override
    public ImmutableSortedMap<K, V> tail() throws NoSuchElementException {
        return left.joinTo(right);
    }

    @Override
    public Iterator<Pair<K, V>> iterator() {
        return new SegmentIterator<Pair<K, V>>(immutableList());
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public Pair<K, V> index(int i) {
        if (left.size() == i) return pair();
        if (i < left.size()) return left.index(i);
        return right.index(i - left.size() - 1);
    }

    public Pair<K, V> pair() {
        return Pair.pair(key, value);
    }
}

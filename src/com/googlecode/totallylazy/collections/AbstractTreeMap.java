package com.googlecode.totallylazy.collections;

import com.googlecode.totallylazy.Callable1;
import com.googlecode.totallylazy.Option;
import com.googlecode.totallylazy.Pair;
import com.googlecode.totallylazy.Predicate;
import com.googlecode.totallylazy.Segment;
import com.googlecode.totallylazy.iterators.SegmentIterator;

import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;

import static com.googlecode.totallylazy.Callers.call;
import static com.googlecode.totallylazy.Unchecked.cast;

public abstract class AbstractTreeMap<K, V, Self extends TreeMap<K, V>> implements TreeMap<K, V> {
    protected final Self left;
    protected final K key;
    protected final V value;
    protected final Self right;
    protected final Comparator<K> comparator;
    protected final int size;
    protected final TreeFactory factory;

    protected AbstractTreeMap(Self left, K key, V value, Self right, Comparator<K> comparator, TreeFactory factory) {
        this.left = left;
        this.key = key;
        this.value = value;
        this.right = right;
        this.comparator = comparator;
        this.factory = factory;
        size = left.size() + right.size() + 1;
    }

    protected Self self(TreeMap<K,V> treeMap) {
        return cast(treeMap);
    }

    @Override
    public K key() {
        return key;
    }

    @Override
    public V value() {
        return value;
    }

    @Override
    public Self left() {
        return left;
    }

    @Override
    public Self left(TreeMap<K,V> newLeft) {
        return cast(factory.create(comparator, newLeft, key, value, right()));
    }

    @Override
    public Self right() {
        return right;
    }

    @Override
    public Self right(TreeMap<K,V> newRight) {
        return cast(factory.create(comparator, left(), key, value, newRight));
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
    public Self put(K key, V value) {
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
    public Self filterKeys(Predicate<? super K> predicate) {
        if (predicate.matches(key))
            return createSelf(left.filterKeys(predicate), key, value, right.filterKeys(predicate), comparator);
        return left.filterKeys(predicate).joinTo(self(right.filterKeys(predicate)));
    }

    private Self createSelf(TreeMap<K, V> left, K key, V value, TreeMap<K, V> right, Comparator<K> comparator) {
        return self(factory.create(comparator, left, key, value, right));
    }

    @Override
    public Self filterValues(Predicate<? super V> predicate) {
        if (predicate.matches(value))
            return createSelf(left.filterValues(predicate), key, value, right.filterValues(predicate), comparator);
        return left.filterValues(predicate).joinTo(self(right.filterValues(predicate)));
    }

    @Override
    public <NewV> TreeMap<K, NewV> mapValues(Callable1<? super V, ? extends NewV> transformer) {
        return factory.create(comparator, left.mapValues(transformer), key, call(transformer, value), right.mapValues(transformer));
    }

    @Override
    public Self remove(K key) {
        int difference = difference(key);
        if (difference == 0) {
            if (left.isEmpty()) return right;
            Pair<? extends TreeMap<K, V>, Pair<K, V>> pair = left.removeLast();
            Self newLeft = self(pair.first());
            Pair<K, V> newRoot = pair.second();
            return createSelf(newLeft, newRoot.first(), newRoot.second(), right, comparator);
        }
        if (difference < 0) return createSelf(left.remove(key), this.key, value, right, comparator);
        return createSelf(left, this.key, value, right.remove(key), comparator);
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
    public Pair<Self, Pair<K, V>> removeFirst() {
        if (left.isEmpty()) return Pair.pair(right, pair());
        final Pair<? extends TreeMap<K, V>, Pair<K, V>> newLeft = left.removeFirst();
        return Pair.<Self, Pair<K, V>>pair(createSelf(newLeft.first(), key, value, right, comparator), newLeft.second());
    }

    @Override
    public Pair<Self, Pair<K, V>> removeLast() {
        if (right.isEmpty()) return Pair.pair(left, pair());
        final Pair<? extends TreeMap<K, V>, Pair<K, V>> newRight = right.removeLast();
        return Pair.<Self, Pair<K, V>>pair(createSelf(left, key, value, newRight.first(), comparator), newRight.second());
    }

    @Override
    public <C extends Segment<Pair<K, V>>> C joinTo(C rest) {
        return cast(left.joinTo(right.joinTo(rest).cons(pair())));
    }

    @Override
    public Self cons(Pair<K, V> newValue) {
        int difference = difference(newValue.first());
        if (difference == 0) return createSelf(left, newValue.first(), newValue.second(), right, comparator);
        if (difference < 0) return createSelf(left.cons(newValue), key, value, right, comparator);
        return createSelf(left, key, value, right.cons(newValue), comparator);
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
        return obj instanceof AbstractTreeMap && value.equals(((AbstractTreeMap) obj).value) && left.equals(((AbstractTreeMap) obj).left) && right.equals(((AbstractTreeMap) obj).right);
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
    public Self tail() throws NoSuchElementException {
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

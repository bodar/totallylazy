package com.googlecode.totallylazy.collections;

import com.googlecode.totallylazy.Segment;
import com.googlecode.totallylazy.comparators.Comparators;
import com.googlecode.totallylazy.iterators.SegmentIterator;

import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;

import static com.googlecode.totallylazy.collections.EmptySet.empty;

public class TreeSet<T> implements ImmutableSet<T> {
    private final ImmutableSet<T> left;
    private final T value;
    private final ImmutableSet<T> right;
    private final Comparator<T> comparator;

    TreeSet(ImmutableSet<T> left, T value, ImmutableSet<T> right, Comparator<T> comparator) {
        this.left = left;
        this.value = value;
        this.right = right;
        this.comparator = comparator;
    }

    static <T extends Comparable<? super T>> TreeSet<T> tree(ImmutableSet<T> left, T value, ImmutableSet<T> right) {
        return new TreeSet<T>(left, value, right, Comparators.<T>ascending());
    }

    static <T> TreeSet<T> tree(ImmutableSet<T> left, T value, ImmutableSet<T> right, Comparator<T> comparator) {
        return new TreeSet<T>(left, value, right, comparator);
    }

    static <T extends Comparable<? super T>> TreeSet<T> tree(T value) {
        return tree(EmptySet.<T>empty(), value, EmptySet.<T>empty());
    }

    static <T> TreeSet<T> tree(T value, Comparator<T> comparator) {
        return tree(empty(comparator), value, empty(comparator), comparator);
    }

    @Override
    public ImmutableList<T> persistentList() {
        return join(ImmutableList.constructors.<T>empty());
    }

    @Override
    public <C extends Segment<T, C>> C join(C rest) {
        return left.join(right.join(rest).cons(value));
    }

    @Override
    public ImmutableSet<T> cons(T newValue) {
        int difference = comparator.compare(newValue, value);
        if (difference == 0) return tree(left, newValue, right, comparator);
        if (difference < 0) return tree(left.cons(newValue), value, right, comparator);
        return tree(left, value, right.cons(newValue), comparator);
    }

    @Override
    public boolean contains(T other) {
        int difference = comparator.compare(other, value);
        if (difference == 0) return value.equals(other);
        if (difference < 0) return left.contains(other);
        return right.contains(other);
    }

    @Override
    public int hashCode() {
        return 19 * value.hashCode() * left.hashCode() * right.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof TreeSet && value.equals(((TreeSet) obj).value) && left.equals(((TreeSet) obj).left) && right.equals(((TreeSet) obj).right);
    }

    @Override
    public String toString() {
        return String.format("(%s %s %s)", left, value, right);
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public T head() throws NoSuchElementException {
        return value;
    }

    @Override
    public ImmutableSet<T> tail() throws NoSuchElementException {
        return left.join(right);
    }

    @Override
    public Iterator<T> iterator() {
        return new SegmentIterator<T, ImmutableList<T>>(persistentList());
    }
}

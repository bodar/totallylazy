package com.googlecode.totallylazy;

import com.googlecode.totallylazy.comparators.Comparators;

import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;

import static com.googlecode.totallylazy.Sequences.sequence;

public abstract class PersistentSet<T> implements Iterable<T>, Constructable<T, PersistentSet<T>>, Deconstructable<T, PersistentSet<T>> {
    protected final Comparator<T> comparator;

    protected PersistentSet(Comparator<T> comparator) {
        this.comparator = comparator;
    }

    public static <T extends Comparable<? super T>> PersistentSet<T> empty(Class<T> aClass) {
        return PersistentSet.<T>empty();
    }

    public static <T extends Comparable<? super T>> PersistentSet<T> empty() {
        return empty(Comparators.<T>ascending());
    }

    public static <T> PersistentSet<T> empty(Comparator<T> comparator) {
        return new Empty<T>(comparator);
    }

    public static <T extends Comparable<? super T>> PersistentSet<T> set(T value) {
        return node(value, PersistentSet.<T>empty(), PersistentSet.<T>empty());
    }

    public static <T> PersistentSet<T> set(T value, Comparator<T> comparator) {
        return node(value, PersistentSet.<T>empty(comparator), PersistentSet.<T>empty(comparator), comparator);
    }

    public static <T extends Comparable<? super T>> PersistentSet<T> set(final T... values) {
        return set(sequence(values));
    }

    public static <T> PersistentSet<T> set(Comparator<T> comparator, final T... values) {
        return set(sequence(values), comparator);
    }

    public static <T extends Comparable<? super T>> PersistentSet<T> set(final Iterable<T> values) {
        return set(values, Comparators.<T>ascending());
    }

    public static <T> PersistentSet<T> set(final Iterable<T> values, Comparator<T> comparator) {
        return sequence(values).fold(PersistentSet.<T>empty(comparator), functions.<T, PersistentSet<T>>cons());
    }

    public static <T extends Comparable<? super T>> PersistentSet<T> node(T value, PersistentSet<T> left, PersistentSet<T> right) {
        return node(value, left, right, Comparators.<T>ascending());
    }

    public static <T> PersistentSet<T> node(T value, PersistentSet<T> left, PersistentSet<T> right, Comparator<T> comparator) {
        return new Node<T>(value, left, right, comparator);
    }

    @Override
    public Iterator<T> iterator() {
        return persistentList().iterator();
    }

    public abstract PersistentList<T> persistentList();

    public abstract boolean contains(T other);

    private static class Empty<T> extends PersistentSet<T> {
        private Empty(Comparator<T> comparator) {
            super(comparator);
        }

        @Override
        public PersistentList<T> persistentList() {
            return PersistentList.empty();
        }

        @Override
        public <C extends Constructable<T, C>> C join(C rest) {
            return rest;
        }

        @Override
        public PersistentSet<T> cons(T newValue) {
            return set(newValue, comparator);
        }

        @Override
        public boolean contains(T other) {
            return false;
        }

        @Override
        public int hashCode() {
            return 31;
        }

        @Override
        public boolean equals(Object obj) {
            return obj instanceof Empty;
        }

        @Override
        public String toString() {
            return "";
        }

        @Override
        public boolean isEmpty() {
            return true;
        }

        @Override
        public T head() throws NoSuchElementException {
            throw new NoSuchElementException();
        }

        @Override
        public PersistentSet<T> tail() throws NoSuchElementException {
            throw new NoSuchElementException();
        }
    }

    private static class Node<T> extends PersistentSet<T> {
        private final T value;
        private final PersistentSet<T> left;
        private final PersistentSet<T> right;

        private Node(T value, PersistentSet<T> left, PersistentSet<T> right, Comparator<T> comparator) {
            super(comparator);
            this.value = value;
            this.left = left;
            this.right = right;
        }

        @Override
        public PersistentList<T> persistentList() {
            return join(PersistentList.<T>empty());
        }

        @Override
        public <C extends Constructable<T, C>> C join(C rest) {
            return left.join(right.join(rest).cons(value));
        }

        @Override
        public PersistentSet<T> cons(T newValue) {
            int difference = comparator.compare(newValue, value);
            if(difference == 0) return node(newValue, left, right, comparator);
            if(difference < 0) return node(value, left.cons(newValue), right, comparator);
            return node(value, left, right.cons(newValue), comparator);
        }

        @Override
        public boolean contains(T other) {
            int difference = comparator.compare(other, value);
            if(difference == 0) return value.equals(other);
            if(difference < 0) return left.contains(other);
            return right.contains(other);
        }

        @Override
        public int hashCode() {
            return 19 * value.hashCode() * left.hashCode() * right.hashCode();
        }

        @Override
        public boolean equals(Object obj) {
            if(!(obj instanceof Node)){
                return false;
            }
            Node other = (Node) obj;
            return value.equals(other.value) && left.equals(other.left) && right.equals(other.right);
        }

        @Override
        public String toString() {
            return String.format("%s -> (%s %s)", value, left, right);
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
        public PersistentSet<T> tail() throws NoSuchElementException {
            return left.join(right);
        }
    }
}

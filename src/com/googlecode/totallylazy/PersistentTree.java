package com.googlecode.totallylazy;

import com.googlecode.totallylazy.comparators.Comparators;

import java.util.Comparator;
import java.util.Iterator;

import static com.googlecode.totallylazy.Sequences.sequence;

public abstract class PersistentTree<T> implements Iterable<T> {
    protected final Comparator<T> comparator;

    protected PersistentTree(Comparator<T> comparator) {
        this.comparator = comparator;
    }

    public static <T extends Comparable<? super T>> PersistentTree<T> empty(Class<T> aClass) {
        return PersistentTree.<T>empty();
    }

    public static <T extends Comparable<? super T>> PersistentTree<T> empty() {
        return empty(Comparators.<T>ascending());
    }

    public static <T> PersistentTree<T> empty(Comparator<T> comparator) {
        return new Empty<T>(comparator);
    }

    public static <T extends Comparable<? super T>> PersistentTree<T> tree(T value) {
        return node(value, PersistentTree.<T>empty(), PersistentTree.<T>empty());
    }

    public static <T> PersistentTree<T> tree(T value, Comparator<T> comparator) {
        return node(value, PersistentTree.<T>empty(comparator), PersistentTree.<T>empty(comparator), comparator);
    }

    public static <T extends Comparable<? super T>> PersistentTree<T> tree(final T... values) {
        return tree(sequence(values));
    }

    public static <T> PersistentTree<T> tree(Comparator<T> comparator, final T... values) {
        return tree(sequence(values), comparator);
    }

    public static <T extends Comparable<? super T>> PersistentTree<T> tree(final Iterable<T> values) {
        return tree(values, Comparators.<T>ascending());
    }

    public static <T> PersistentTree<T> tree(final Iterable<T> values, Comparator<T> comparator) {
        return sequence(values).fold(PersistentTree.<T>empty(comparator), PersistentTree.<T>insert());
    }

    public static <T extends Comparable<? super T>> PersistentTree<T> node(T value, PersistentTree<T> left, PersistentTree<T> right) {
        return node(value, left, right, Comparators.<T>ascending());
    }

    public static <T> PersistentTree<T> node(T value, PersistentTree<T> left, PersistentTree<T> right, Comparator<T> comparator) {
        return new Node<T>(value, left, right, comparator);
    }

    @Override
    public Iterator<T> iterator() {
        return persistentList().iterator();
    }

    public abstract PersistentList<T> persistentList();

    public abstract PersistentList<T> join(PersistentList<T> cons);

    public abstract PersistentTree<T> insert(T newValue);

    public static <T> Function2<PersistentTree<T>, T, PersistentTree<T>> insert() {
        return new Function2<PersistentTree<T>, T, PersistentTree<T>>() {
            @Override
            public PersistentTree<T> call(PersistentTree<T> tree, T t) throws Exception {
                return tree.insert(t);
            }
        };
    }

    public abstract boolean contains(T other);

    private static class Empty<T> extends PersistentTree<T> {
        private Empty(Comparator<T> comparator) {
            super(comparator);
        }

        @Override
        public PersistentList<T> persistentList() {
            return PersistentList.empty();
        }

        @Override
        public PersistentList<T> join(PersistentList<T> cons) {
            return cons;
        }

        @Override
        public PersistentTree<T> insert(T newValue) {
            return tree(newValue, comparator);
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
    }

    private static class Node<T> extends PersistentTree<T> {
        private final T value;
        private final PersistentTree<T> left;
        private final PersistentTree<T> right;

        private Node(T value, PersistentTree<T> left, PersistentTree<T> right, Comparator<T> comparator) {
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
        public PersistentList<T> join(PersistentList<T> list) {
            return left.join(right.join(list).cons(value));
        }

        @Override
        public PersistentTree<T> insert(T newValue) {
            int difference = comparator.compare(newValue, value);
            if(difference == 0) return node(newValue, left, right, comparator);
            if(difference < 0) return node(value, left.insert(newValue), right, comparator);
            return node(value, left, right.insert(newValue), comparator);
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
    }
}

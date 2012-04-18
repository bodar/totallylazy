package com.googlecode.totallylazy.collections;

import com.googlecode.totallylazy.Constructable;

import java.util.Comparator;
import java.util.NoSuchElementException;

class TreeNode<T> extends LLRBTree<T> {
    final Colour colour;
    final PersistentSet<T> left;
    final T value;
    final PersistentSet<T> right;

    TreeNode(Colour colour, PersistentSet<T> left, T value, PersistentSet<T> right, Comparator<T> comparator) {
        super(comparator);
        this.left = left;
        this.value = value;
        this.right = right;
        this.colour = colour;
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
        if (difference == 0) return node(colour, left, newValue, right, comparator);
        if (difference < 0) return balanceLeft(node(colour, left.cons(newValue), value, right, comparator));
        return balanceRight(node(colour, left, value, right.cons(newValue), comparator));
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
        return 19 * colour.hashCode() * value.hashCode() * left.hashCode() * right.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof TreeNode)) {
            return false;
        }
        TreeNode other = (TreeNode) obj;
        return colour.equals(other.colour) && value.equals(other.value) && left.equals(other.left) && right.equals(other.right);
    }

    @Override
    public String toString() {
        return String.format("(%s %s %s %s)", left, colour, value, right);
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

package com.googlecode.totallylazy.collections;

import com.googlecode.totallylazy.Unchecked;
import com.googlecode.totallylazy.comparators.Comparators;

import java.util.Comparator;
import java.util.Iterator;

import static com.googlecode.totallylazy.Sequences.sequence;
import static com.googlecode.totallylazy.collections.Colour.Black;
import static com.googlecode.totallylazy.collections.Colour.Red;

public abstract class LLRBTree<T> implements PersistentSet<T> {
    protected final Comparator<T> comparator;

    protected LLRBTree(Comparator<T> comparator) {
        this.comparator = comparator;
    }

    public static <T extends Comparable<? super T>> PersistentSet<T> set(T value) {
        return black(EmptyTree.<T>empty(), value, EmptyTree.<T>empty());
    }

    public static <T> PersistentSet<T> set(T value, Comparator<T> comparator) {
        return black(EmptyTree.<T>empty(comparator), value, EmptyTree.<T>empty(comparator), comparator);
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
        return sequence(values).fold(EmptyTree.<T>empty(comparator), functions.<T, PersistentSet<T>>cons());
    }

    public static <T extends Comparable<? super T>> PersistentSet<T> black(PersistentSet<T> left, T value, PersistentSet<T> right) {
        return node(Black, left, value, right);
    }

    public static <T extends Comparable<? super T>> PersistentSet<T> red(PersistentSet<T> left, T value, PersistentSet<T> right) {
        return node(Red, left, value, right);
    }

    public static <T extends Comparable<? super T>> TreeNode<T> node(Colour colour, PersistentSet<T> left, T value, PersistentSet<T> right) {
        return node(colour, left, value, right, Comparators.<T>ascending());
    }

    public static <T> TreeNode<T> node(Colour colour, PersistentSet<T> left, T value, PersistentSet<T> right, Comparator<T> comparator) {
        return new TreeNode<T>(colour, left, value, right, comparator);
    }

    public static <T> PersistentSet<T> black(PersistentSet<T> left, T value, PersistentSet<T> right, Comparator<T> comparator) {
        return node(Black, left, value, right, comparator);
    }

    public static <T> PersistentSet<T> red(PersistentSet<T> left, T value, PersistentSet<T> right, Comparator<T> comparator) {
        return node(Red, left, value, right, comparator);
    }

    @Override
    public Iterator<T> iterator() {
        return persistentList().iterator();
    }

    public static <T> PersistentSet<T> balanceLeft(TreeNode<T> node) {
        if (colour(node, Black)) {
            if (colour(node.left, Red)) {
                TreeNode<T> left = Unchecked.cast(node.left);
                if (colour(left.left, Red)) {
                    TreeNode<T> leftleft = Unchecked.cast(left.left);
                    return red(set(leftleft.value, node.comparator), left.value, set(node.value, node.comparator), node.comparator);
                }
            }
        }
        return node;
    }

    public static <T> PersistentSet<T> balanceRight(TreeNode<T> node) {
        if (colour(node, Black)) {
            if (colour(node.left, Red) && colour(node.right, Red)) {
                TreeNode<T> left = Unchecked.cast(node.left);
                TreeNode<T> right = Unchecked.cast(node.right);
                return red(set(left.value, node.comparator), node.value, set(right.value, node.comparator), node.comparator);
            }
        }
        if (node.left instanceof TreeNode && colour(node.right, Red)) {
            TreeNode<T> left = Unchecked.cast(node.left);
            TreeNode<T> right = Unchecked.cast(node.right);
            return node(node.colour, red(set(left.value, node.comparator), node.value, EmptyTree.<T>empty(node.comparator), node.comparator), right.value, EmptyTree.<T>empty(node.comparator), node.comparator);
        }
        return node;
    }

    private static boolean colour(final PersistentSet<?> persistentSet, final Colour value) {
        return persistentSet instanceof TreeNode && ((TreeNode) persistentSet).colour.equals(value);
    }

}

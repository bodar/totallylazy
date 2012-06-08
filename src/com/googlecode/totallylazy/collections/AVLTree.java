package com.googlecode.totallylazy.collections;

import com.googlecode.totallylazy.Function2;
import com.googlecode.totallylazy.Unchecked;
import com.googlecode.totallylazy.comparators.Comparators;

import java.util.Comparator;

import static com.googlecode.totallylazy.Unchecked.cast;

public interface AVLTree<K, V> extends ImmutableMap<K, V> {
    int height();

    int balance();

    class Empty<K, V> extends EmptyMap<K, V> implements AVLTree<K, V> {
        protected Empty(final Function2<K, V, AVLTree<K, V>> creator) {
            super(creator);
        }

        @Override
        public int height() {
            return 0;
        }

        @Override
        public int balance() {
            return 0;
        }
    }

    class Node<K, V> extends TreeMap<K, V> implements AVLTree<K, V> {
        private final int height;
        final AVLTree<K, V> left;
        final AVLTree<K, V> right;

        private Node(AVLTree<K, V> left, K key, V value, AVLTree<K, V> right, Comparator<K> comparator) {
            super(left, key, value, right, comparator);
            this.left = left;
            this.right = right;
            height = Math.max(left.height(), right.height()) + 1;
        }

        static <K, V> Node<K, V> node(AVLTree<K, V> left, K key, V value, AVLTree<K, V> right, Comparator<K> comparator) {
            return new Node<K, V>(left, key, value, right, comparator);
        }

        @Override
        <K, V> Node<K, V> create(ImmutableMap<K, V> left, K key, V value, ImmutableMap<K, V> right, Comparator<K> comparator) {
            return balance(node(Unchecked.<AVLTree<K, V>>cast(left), key, value, Unchecked.<AVLTree<K, V>>cast(right), comparator));
        }

        // http://upload.wikimedia.org/wikipedia/commons/thumb/f/f5/AVL_Tree_Rebalancing.svg/350px-AVL_Tree_Rebalancing.svg.png
        private static <K, V> Node<K, V> balance(Node<K, V> node) {
            if (node.balance() == -2) {
                return balanceRight(node);
            }
            if (node.balance() == 2) {
                return balanceLeft(node);
            }
            return node;
        }

        private static <K, V> Node<K, V> balanceLeft(Node<K, V> node) {
            if (node.left.balance() == -1) {
                return balanceLeftRight(node);
            }
            if (node.left.balance() == 1) {
                return balanceLeftLeft(node);
            }
            return node;
        }

        private static <K, V> Node<K, V> balanceRight(Node<K, V> node) {
            if (node.right.balance() == 1) {
                return balanceRightLeft(node);
            }
            if (node.right.balance() == -1) {
                return balanceRightRight(node);
            }
            return node;
        }

        private static <K, V> Node<K, V> balanceLeftLeft(Node<K, V> parent) {
            AVLTree<K, V> c = asNode(parent.left).right;
            Node<K, V> five = parent.left(c);
            return asNode(parent.left).right(five);
        }

        private static <K, V> Node<K, V> balanceLeftRight(Node<K, V> parent) {
            AVLTree<K, V> b = asNode(asNode(parent.left).right).left;
            Node<K, V> three = asNode(parent.left).right(b);
            Node<K, V> four = asNode(asNode(parent.left).right).left(three);
            return balanceLeftLeft(parent.left(four));
        }

        private static <K, V> Node<K, V> balanceRightRight(Node<K, V> parent) {
            AVLTree<K, V> b = asNode(parent.right).left;
            Node<K, V> three = parent.right(b);
            return asNode(parent.right).left(three);
        }

        private static <K, V> Node<K, V> balanceRightLeft(Node<K, V> parent) {
            AVLTree<K, V> c = asNode(asNode(parent.right).left).right;
            Node<K, V> five = asNode(parent.right).left(c);
            Node<K, V> four = asNode(asNode(parent.right).left).right(five);
            return balanceRightRight(parent.right(four));
        }

        private static <K, V> Node<K, V> asNode(AVLTree<K, V> node) {
            return cast(node);
        }

        @Override
        public int height() {
            return height;
        }

        @Override
        public int balance() {
            return left.height() - right.height();
        }

        private Node<K, V> left(AVLTree<K, V> newLeft) {
            return node(newLeft, key, value, right, comparator);
        }

        private Node<K, V> right(AVLTree<K, V> newRight) {
            return node(left, key, value, newRight, comparator);
        }
    }

    class constructors {
        public static <K extends Comparable<? super K>, V> Empty<K, V> empty() {
            return empty(Comparators.<K>ascending());
        }

        public static <K extends Comparable<? super K>, V> AVLTree<K, V> node(K k, V v) {
            return node(k, v, Comparators.<K>ascending());
        }

        public static <K, V> Empty<K, V> empty(Comparator<K> comparator) {
            return new Empty<K, V>(functions.<K, V>creator(comparator));
        }

        public static <K, V> Node<K, V> node(K k, V v, Comparator<K> comparator) {
            return node(constructors.<K, V>empty(comparator), k, v, constructors.<K, V>empty(comparator), comparator);
        }

        public static <K, V> Node<K, V> node(AVLTree<K, V> left, K k, V v, AVLTree<K, V> right, final Comparator<K> comparator) {
            return Node.node(left, k, v, right, comparator);
        }
    }

    class functions {
        public static <K, V> Function2<K, V, AVLTree<K, V>> creator(final Comparator<K> comparator) {
            return new Function2<K, V, AVLTree<K, V>>() {
                @Override
                public AVLTree<K, V> call(K k, V v) throws Exception {
                    return constructors.node(k, v, comparator);
                }
            };
        }
    }
}

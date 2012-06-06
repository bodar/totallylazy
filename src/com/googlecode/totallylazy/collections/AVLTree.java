package com.googlecode.totallylazy.collections;

import com.googlecode.totallylazy.Function2;
import com.googlecode.totallylazy.Unchecked;
import com.googlecode.totallylazy.comparators.Comparators;

import java.util.Comparator;

import static com.googlecode.totallylazy.Unchecked.cast;

public interface AVLTree<K, V> extends ImmutableMap<K, V> {
    int height();

    class Empty<K, V> extends EmptyMap<K, V> implements AVLTree<K, V> {
        protected Empty(final Function2<K, V, AVLTree<K, V>> creator) {
            super(creator);
        }

        @Override
        public int height() {
            return 0;
        }
    }

    class Node<K, V> extends TreeMap<K, V> implements AVLTree<K, V> {
        private final int height;

        Node(AVLTree<K, V> left, K key, V value, AVLTree<K, V> right, Comparator<K> comparator) {
            super(left, key, value, right, comparator);
            height = Math.max(left.height(), right.height()) + 1;
        }

        @Override
        <K, V> Node<K, V> create(ImmutableMap<K, V> left, K key, V value, ImmutableMap<K, V> right, Comparator<K> comparator) {
            AVLTree<K, V> leftAvl = Unchecked.<AVLTree<K, V>>cast(left);
            AVLTree<K, V> rightAvl = Unchecked.<AVLTree<K, V>>cast(right);
            return balance(new Node<K, V>(leftAvl, key, value, rightAvl, comparator));
        }

        private static <K,V> Node<K,V> balance(Node<K,V> node) {
            if(balanceFactor())
        }

        private static <K, V> int balanceFactor(Node<K,V> node) {
            return node.left.height() - rightAvl.height();
        }


        @Override
        public int height() {
            return height;
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
            return new Node<K, V>(left, k, v, right, comparator);
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

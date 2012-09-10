package com.googlecode.totallylazy.collections;

import com.googlecode.totallylazy.Callable1;
import com.googlecode.totallylazy.Function1;
import com.googlecode.totallylazy.Functions;
import com.googlecode.totallylazy.Pair;

import static com.googlecode.totallylazy.Sequences.sequence;
import static com.googlecode.totallylazy.collections.TreeZipper.Breadcrumb.breadcrumb;
import static com.googlecode.totallylazy.collections.TreeZipper.Direction.left;
import static com.googlecode.totallylazy.collections.TreeZipper.Direction.right;
import static java.lang.String.format;

public class TreeZipper<K, V> {
    public final TreeMap<K, V> focus;
    public final ImmutableList<Breadcrumb<K, V>> breadcrumbs;

    private TreeZipper(TreeMap<K, V> focus, ImmutableList<Breadcrumb<K, V>> breadcrumbs) {
        this.focus = focus;
        this.breadcrumbs = breadcrumbs;
    }

    public static <K, V> TreeZipper<K, V> zipper(TreeMap<K, V> focus) {
        return new TreeZipper<K, V>(focus, ImmutableList.constructors.<Breadcrumb<K, V>>empty());
    }

    private static <K, V> TreeZipper<K, V> zipper(TreeMap<K, V> focus, ImmutableList<Breadcrumb<K, V>> crumbs) {
        return new TreeZipper<K, V>(focus, crumbs);
    }

    public TreeZipper<K, V> left() {
        return zipper(focus.left(), breadcrumbs.cons(breadcrumb(left, focus.head(), focus.right())));
    }

    public TreeZipper<K, V> right() {
        return zipper(focus.right(), breadcrumbs.cons(breadcrumb(right, focus.head(), focus.left())));
    }

    public TreeZipper<K, V> up() {
        final Breadcrumb<K, V> breadcrumb = breadcrumbs.head();
        final TreeMap<K, V> left = breadcrumb.direction == Direction.left ? focus : breadcrumb.other;
        final TreeMap<K, V> right = breadcrumb.direction == Direction.left ? breadcrumb.other : focus;
        return zipper(focus.factory().create(focus.comparator(), breadcrumb.parent.first(), breadcrumb.parent.second(), left, right), breadcrumbs.tail());
    }

    public TreeZipper<K, V> toStart() {
        if (breadcrumbs.isEmpty()) return this;
        return up().toStart();
    }

    public TreeMap<K, V> toTreeMap() {
        return toStart().focus;
    }

    public TreeZipper<K, V> modify(Callable1<? super TreeMap<K, V>, ? extends TreeMap<K, V>> callable) {
        return zipper(Functions.call(callable, focus), breadcrumbs).toStart();
    }

    public TreeZipper<K, V> replace(K key, V value) {
        return modify(functions.replace(key, value));
    }

    public enum Direction {
        left, right
    }

    public static final class Breadcrumb<K, V> {
        public final Direction direction;
        public final Pair<K, V> parent;
        public final TreeMap<K, V> other;

        private Breadcrumb(Direction direction, Pair<K, V> parent, TreeMap<K, V> other) {
            this.parent = parent;
            this.other = other;
            this.direction = direction;
        }

        public static <K, V> Breadcrumb<K, V> breadcrumb(Direction direction, Pair<K, V> parent, TreeMap<K, V> other) {
            return new Breadcrumb<K, V>(direction, parent, other);
        }

        @Override
        public String toString() {
            return format("direction(%s), parent(%s), other(%s)", direction, parent, other);
        }

        @Override
        public int hashCode() {
            return sequence(direction, parent, other).hashCode();
        }

        @Override
        public boolean equals(Object obj) {
            return obj instanceof Breadcrumb &&
                    ((Breadcrumb) obj).direction.equals(direction) &&
                    ((Breadcrumb) obj).parent.equals(parent) &&
                    ((Breadcrumb) obj).other.equals(other);
        }
    }

    static class functions {
        public static <K, V> Function1<TreeMap<K, V>, TreeMap<K, V>> replace(final K key, final V value) {
            return new Function1<TreeMap<K, V>, TreeMap<K, V>>() {
                @Override
                public TreeMap<K, V> call(TreeMap<K, V> focus) throws Exception {
                    return focus.factory().create(focus.comparator(), key, value, focus.left(), focus.right());
                }
            };
        }
    }
}
package com.googlecode.totallylazy.collections;

import com.googlecode.totallylazy.Callable1;
import com.googlecode.totallylazy.Function2;
import com.googlecode.totallylazy.Functions;
import com.googlecode.totallylazy.Mapper;
import com.googlecode.totallylazy.Option;
import com.googlecode.totallylazy.Pair;
import com.googlecode.totallylazy.annotations.tailrec;

import static com.googlecode.totallylazy.Option.none;
import static com.googlecode.totallylazy.Option.some;
import static com.googlecode.totallylazy.Predicates.is;
import static com.googlecode.totallylazy.Predicates.where;
import static com.googlecode.totallylazy.Sequences.sequence;
import static com.googlecode.totallylazy.collections.TreeZipper.Breadcrumb.breadcrumb;
import static com.googlecode.totallylazy.collections.TreeZipper.Direction.left;
import static com.googlecode.totallylazy.collections.TreeZipper.Direction.right;
import static com.googlecode.totallylazy.collections.TreeZipper.functions.direction;
import static java.lang.String.format;

public class TreeZipper<K, V> implements Zipper<Pair<K, V>> {
    public final TreeMap<K, V> focus;
    public final PersistentList<Breadcrumb<K, V>> breadcrumbs;

    private TreeZipper(TreeMap<K, V> focus, PersistentList<Breadcrumb<K, V>> breadcrumbs) {
        this.focus = focus;
        this.breadcrumbs = breadcrumbs;
    }

    public static <K, V> TreeZipper<K, V> zipper(TreeMap<K, V> focus) {
        return new TreeZipper<K, V>(focus, PersistentList.constructors.<Breadcrumb<K, V>>empty());
    }

    private static <K, V> TreeZipper<K, V> zipper(TreeMap<K, V> focus, PersistentList<Breadcrumb<K, V>> crumbs) {
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

    @tailrec
    public TreeZipper<K, V> top() {
        if (breadcrumbs.isEmpty()) return this;
        return up().top();
    }

    public TreeMap<K, V> toTreeMap() {
        return top().focus;
    }

    public TreeZipper<K, V> modify(Callable1<? super TreeMap<K, V>, ? extends TreeMap<K, V>> callable) {
        TreeMap<K, V> result = Functions.call(callable, focus);
        TreeZipper<K, V> newZipper = zipper(result, breadcrumbs);
        if (newZipper.focus.isEmpty()) return newZipper.up();
        return newZipper;
    }

    public TreeZipper<K, V> replace(K key, V value) {
        return modify(functions.replace(key, value));
    }

    public TreeZipper<K, V> delete() {
        return remove();
    }

    public TreeZipper<K, V> remove() {
        return modify(functions.<K, V>remove());
    }

    @tailrec
    public TreeZipper<K, V> first() {
        if (isFirst()) return this;
        return left().first();
    }

    @tailrec
    public TreeZipper<K, V> last() {
        if (isLast()) return this;
        return right().last();
    }

    @Override
    public boolean isFirst() {
        return focus.left().isEmpty();
    }

    @Override
    public boolean isLast() {
        return focus.right().isEmpty();
    }

    @Override
    public int index() {
        return focus.indexOf(value()) + breadcrumbs.filter(where(direction, is(right))).fold(0, new Function2<Integer, Breadcrumb<K, V>, Integer>() {
            @Override
            public Integer call(Integer integer, Breadcrumb<K, V> breadcrumb) throws Exception {
                return integer + breadcrumb.other.size() + 1;
            }
        });
    }

    public boolean isTop() {
        return breadcrumbs.isEmpty();
    }

    public TreeZipper<K, V> next() {
        if (focus.right().isEmpty()) return backtrack(right).up();
        return right().first();
    }

    public Option<TreeZipper<K, V>> nextOption() {
        try {
            return some(next());
        } catch (Exception e) {
            return none();
        }
    }

    public TreeZipper<K, V> previous() {
        if (focus.left().isEmpty()) return backtrack(left).up();
        return left().last();
    }

    public Option<TreeZipper<K, V>> previousOption() {
        try {
            return some(previous());
        } catch (Exception e) {
            return none();
        }
    }

    @tailrec
    private TreeZipper<K, V> backtrack(final Direction direction) {
        if (breadcrumbs.head().direction.equals(direction)) return up().backtrack(direction);
        return this;
    }

    @Override
    public String toString() {
        return "TreeZipper{" +
                "focus=" + focus +
                ", breadcrumbs=" + breadcrumbs +
                '}';
    }

    public Pair<K, V> pair() {
        return Pair.pair(focus.key(), focus.value());
    }

    @Override
    public Pair<K, V> value() {
        return pair();
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

    public static class functions extends TreeMap.functions {
        public static Mapper<Breadcrumb<?, ?>, Direction> direction = new Mapper<Breadcrumb<?, ?>, Direction>() {
            @Override
            public Direction call(Breadcrumb<?, ?> breadcrumb) throws Exception {
                return breadcrumb.direction;
            }
        };
    }

}
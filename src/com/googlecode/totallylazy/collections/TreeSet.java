package com.googlecode.totallylazy.collections;

import com.googlecode.totallylazy.Callable1;
import com.googlecode.totallylazy.Callers;
import com.googlecode.totallylazy.Function1;
import com.googlecode.totallylazy.Option;
import com.googlecode.totallylazy.Pair;
import com.googlecode.totallylazy.Predicate;
import com.googlecode.totallylazy.Segment;
import com.googlecode.totallylazy.Unchecked;
import com.googlecode.totallylazy.comparators.Comparators;

import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;

import static com.googlecode.totallylazy.Pair.pair;

public class TreeSet<A> implements ImmutableSet<A> {
    protected final ImmutableSet<A> left;
    protected final A value;
    protected final ImmutableSet<A> right;
    protected final Comparator<A> comparator;

    TreeSet(ImmutableSet<A> left, A value, ImmutableSet<A> right, Comparator<A> comparator) {
        this.left = left;
        this.value = value;
        this.right = right;
        this.comparator = comparator;
    }

    <A> TreeSet<A> create(ImmutableSet<A> left, A value, ImmutableSet<A> right, Comparator<A> comparator) {
        return tree(left, value, right, comparator);
    }

    static <A extends Comparable<? super A>> TreeSet<A> tree(A value) {
        return tree(TreeSet.<A>empty(), value, TreeSet.<A>empty());
    }

    static <A extends Comparable<? super A>> EmptySet<A> empty() {
        return EmptySet.<A>emptySet(TreeSet.<A>tree());
    }

    private static <A extends Comparable<? super A>> Function1<A, ImmutableSet<A>> tree() {
        return new Function1<A, ImmutableSet<A>>() {
            @Override
            public ImmutableSet<A> call(A a) throws Exception {
                return tree(a);
            }
        };
    }

    static <A extends Comparable<? super A>> TreeSet<A> tree(ImmutableSet<A> left, A value, ImmutableSet<A> right) {
        return new TreeSet<A>(left, value, right, Comparators.<A>ascending());
    }

    static <A> TreeSet<A> tree(ImmutableSet<A> left, A value, ImmutableSet<A> right, Comparator<A> comparator) {
        return new TreeSet<A>(left, value, right, comparator);
    }

    static <A> TreeSet<A> tree(A value, Comparator<A> comparator) {
        return tree(TreeSet.<A>empty(comparator), value, TreeSet.<A>empty(comparator), comparator);
    }

    static <A> ImmutableSet<A> empty(Comparator<A> comparator) {
        return EmptySet.<A>emptySet(TreeSet.<A>tree(comparator));
    }

    private static <A> Function1<A, ImmutableSet<A>> tree(final Comparator<A> comparator) {
        return new Function1<A, ImmutableSet<A>>() {
            @Override
            public ImmutableSet<A> call(A a) throws Exception {
                return tree(a, comparator);
            }
        };
    }

    @Override
    public ImmutableList<A> immutableList() {
        return joinTo(ImmutableList.constructors.<A>empty());
    }

    @Override
    public ImmutableSet<A> put(A value) {
        return cons(value);
    }

    @Override
    public Option<A> find(Predicate<? super A> predicate) {
        if (predicate.matches(value)) return Option.some(value);
        Option<A> left = this.left.find(predicate);
        if (left.isEmpty()) return right.find(predicate);
        return left;
    }

    @Override
    public ImmutableSet<A> filter(Predicate<? super A> predicate) {
        if (predicate.matches(value))
            return create(left.filter(predicate), value, right.filter(predicate), comparator);
        return left.filter(predicate).joinTo(right.filter(predicate));
    }

    @Override
    public <NewA> ImmutableSet<NewA> map(Callable1<? super A, ? extends NewA> transformer) {
        return create(left.map(transformer), Callers.call(transformer, value), right.map(transformer), Unchecked.<Comparator<NewA>>cast(comparator));
    }

    @Override
    public ImmutableSet<A> remove(A value) {
        int difference = difference(value);
        if (difference == 0) {
            if (left.isEmpty()) return right;
            Pair<ImmutableSet<A>, A> pair = left.removeMaximum();
            ImmutableSet<A> newLeft = pair.first();
            A newRoot = pair.second();
            return create(newLeft, newRoot, right, comparator);
        }
        if (difference < 0) return create(left.remove(value), this.value, right, comparator);
        return create(left, this.value, right.remove(value), comparator);
    }

    private int difference(A other) {
        return comparator.compare(other, this.value);
    }

    @Override
    public Pair<ImmutableSet<A>, A> removeMinimum() {
        if (left.isEmpty()) return pair(right, value);
        final Pair<ImmutableSet<A>, A> newLeft = left.removeMinimum();
        return Pair.<ImmutableSet<A>, A>pair(create(newLeft.first(), value, right, comparator), newLeft.second());
    }

    @Override
    public Pair<ImmutableSet<A>, A> removeMaximum() {
        if (right.isEmpty()) return pair(left, value);
        final Pair<ImmutableSet<A>, A> newRight = right.removeMaximum();
        return Pair.<ImmutableSet<A>, A>pair(create(left, value, newRight.first(), comparator), newRight.second());
    }

    @Override
    public <C extends Segment<A, C>> C joinTo(C rest) {
        return left.joinTo(right.joinTo(rest).cons(value));
    }

    @Override
    public ImmutableSet<A> cons(A newValue) {
        int difference = difference(newValue);
        if (difference == 0) return create(left, newValue, right, comparator);
        if (difference < 0) return create(left.cons(newValue), value, right, comparator);
        return create(left, value, right.cons(newValue), comparator);
    }

    @Override
    public boolean contains(A other) {
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
        return obj instanceof TreeSet && value.equals(((TreeSet) obj).value) && left.equals(((TreeSet) obj).left) && right.equals(((TreeSet) obj).right);
    }

    @Override
    public String toString() {
        return String.format("(%s %s %s)", left, right);
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public A head() throws NoSuchElementException {
        return value;
    }

    @Override
    public ImmutableSet<A> tail() throws NoSuchElementException {
        return left.joinTo(right);
    }

    @Override
    public Iterator<A> iterator() {
        return immutableList().iterator();
    }
}

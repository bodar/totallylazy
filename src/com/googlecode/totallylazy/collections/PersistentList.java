package com.googlecode.totallylazy.collections;

import com.googlecode.totallylazy.Callable1;
import com.googlecode.totallylazy.Callables;
import com.googlecode.totallylazy.Callers;
import com.googlecode.totallylazy.Option;
import com.googlecode.totallylazy.Predicate;
import com.googlecode.totallylazy.Segment;
import com.googlecode.totallylazy.Sequence;
import com.googlecode.totallylazy.iterators.SegmentIterator;

import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import static com.googlecode.totallylazy.Callers.call;
import static com.googlecode.totallylazy.Predicates.in;
import static com.googlecode.totallylazy.Predicates.is;
import static com.googlecode.totallylazy.Predicates.not;
import static com.googlecode.totallylazy.Predicates.onlyOnce;
import static com.googlecode.totallylazy.Predicates.where;
import static com.googlecode.totallylazy.Sequences.sequence;
import static com.googlecode.totallylazy.Sequences.zip;
import static com.googlecode.totallylazy.Sets.set;
import static com.googlecode.totallylazy.Unchecked.cast;
import static com.googlecode.totallylazy.collections.ListZipper.zipper;

public abstract class PersistentList<T> implements ImmutableList<T> {
    static final Empty EMPTY = new Empty();

    static <T> PersistentList<T> empty() {
        return cast(EMPTY);
    }

    static <T> PersistentList<T> cons(T head, ImmutableList<T> tail) {
        return Node.node(head, tail);
    }

    @Override
    public ImmutableList<T> cons(T head) {
        return cons(head, this);
    }

    @Override
    public Sequence<T> toSequence() {
        return sequence(this);
    }

    @Override
    public ImmutableList<T> removeAll(Iterable<T> values) {
        return filter(not(in(set(values))));
    }

    @Override
    public ImmutableList<T> add(T value) {
        return constructors.list(toSequence().add(value));
    }

    @Override
    public List<T> toList() {
        return toSequence().toList();
    }

    @Override
    public Iterator<T> iterator() {
        return new SegmentIterator<T>(this);
    }

    private static class Empty<T> extends PersistentList<T> {
        private Empty() {
        }

        @Override
        public T head() {
            throw new NoSuchElementException();
        }

        @Override
        public ImmutableList<T> tail() {
            throw new NoSuchElementException();
        }

        @Override
        public <C extends Segment<T>> C joinTo(C rest) {
            return rest;
        }

        @Override
        public ImmutableList<T> remove(T value) {
            return this;
        }

        @Override
        public int size() {
            return 0;
        }

        @Override
        public <S> ImmutableList<S> map(Callable1<? super T, ? extends S> callable) {
            return cast(this);
        }

        @Override
        public Option<T> find(Predicate<? super T> predicate) {
            return Option.none();
        }

        @Override
        public ImmutableList<T> filter(Predicate<? super T> predicate) {
            return this;
        }

        @Override
        public boolean contains(T other) {
            return false;
        }

        @Override
        public boolean exists(Predicate<? super T> predicate) {
            return false;
        }

        @Override
        public boolean isEmpty() {
            return true;
        }

        @Override
        public String toString() {
            return "[]";
        }
    }

    private static class Node<T> extends PersistentList<T> {
        private final T head;
        private final ImmutableList<T> tail;
        private final int size;

        private Node(T head, ImmutableList<T> tail) {
            this.head = head;
            this.tail = tail;
            size = 1 + tail.size();
        }

        private static <T> Node<T> node(T head, ImmutableList<T> tail) {
            return new Node<T>(head, tail);
        }

        @Override
        public T head() {
            return head;
        }

        @Override
        public ImmutableList<T> tail() {
            return tail;
        }

        @Override
        public <C extends Segment<T>> C joinTo(C rest) {
            return cast(tail.joinTo(rest).cons(head));
        }

        @Override
        public ImmutableList<T> remove(T value) {
            ListZipper<T> zipper = zipper(this);
            while (!zipper.atEnd()){
                if(zipper.current().equals(value)) return zipper.delete().toList();
                zipper = zipper.right();
            }
            return this;
        }

        @Override
        public int size() {
            return size;
        }

        @Override
        public <S> ImmutableList<S> map(Callable1<? super T, ? extends S> callable) {
            return node(call(callable, head), tail().map(callable));
        }

        @Override
        public Option<T> find(Predicate<? super T> predicate) {
            return toSequence().find(predicate);
        }

        @Override
        public ImmutableList<T> filter(Predicate<? super T> predicate) {
            return constructors.list(toSequence().filter(predicate));
        }

        @Override
        public boolean contains(T other) {
            return toSequence().contains(other);
        }

        @Override
        public boolean exists(Predicate<? super T> predicate) {
            return toSequence().exists(predicate);
        }

        @Override
        public boolean isEmpty() {
            return false;
        }

        @Override
        public String toString() {
            return toSequence().toString("::");
        }

        @Override
        public int hashCode() {
            return toSequence().hashCode();
        }

        @Override
        public boolean equals(Object obj) {
            return obj instanceof Node && ((Node) obj).head.equals(head) && ((Node) obj).tail.equals(tail);
        }
    }
}
package com.googlecode.totallylazy.collections;

import com.googlecode.totallylazy.Function2;
import com.googlecode.totallylazy.Segment;
import com.googlecode.totallylazy.Sequence;
import com.googlecode.totallylazy.iterators.SegmentIterator;

import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import static com.googlecode.totallylazy.Predicates.in;
import static com.googlecode.totallylazy.Predicates.is;
import static com.googlecode.totallylazy.Predicates.not;
import static com.googlecode.totallylazy.Predicates.onlyOnce;
import static com.googlecode.totallylazy.Sequences.sequence;
import static com.googlecode.totallylazy.Sets.set;
import static com.googlecode.totallylazy.Unchecked.cast;

public abstract class PersistentList<T> implements ImmutableList<T> {
    static final Empty EMPTY = new Empty();

    static <T> PersistentList<T> empty() {
        return cast(EMPTY);
    }

    static <T> PersistentList<T> cons(T head, ImmutableList<T> tail) {
        return new Node<T>(head, tail);
    }

    @Override
    public ImmutableList<T> cons(T head) {
        return cons(head, this);
    }

    @Override
    public ImmutableList<T> remove(T value) {
        return constructors.list(toSequence().filter(not(onlyOnce(is(value)))));
    }

    @Override
    public Sequence<T> toSequence() {
        return sequence(this);
    }

    @Override
    public ImmutableList<T> removeAll(Iterable<T> values) {
        return constructors.list(toSequence().filter(not(in(set(values)))));
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
        return new SegmentIterator<T, ImmutableList<T>>(this);
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
        public <C extends Segment<T, C>> C join(C rest) {
            return rest;
        }

        @Override
        public int size() {
            return 0;
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

        @Override
        public T head() {
            return head;
        }

        @Override
        public ImmutableList<T> tail() {
            return tail;
        }

        @Override
        public <C extends Segment<T, C>> C join(C rest) {
            return tail.join(rest).cons(head);
        }

        @Override
        public int size() {
            return size;
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
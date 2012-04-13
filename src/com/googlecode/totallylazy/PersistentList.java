package com.googlecode.totallylazy;

import com.googlecode.totallylazy.iterators.ReadOnlyIterator;

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

public abstract class PersistentList<T> implements Iterable<T> {
    private static final Empty EMPTY = new Empty();

    public static <T> PersistentList<T> empty() {
        return cast(EMPTY);
    }

    public static <T> Node<T> cons(T head, PersistentList<T> tail) {
        return new Node<T>(head, tail);
    }

    public static <T> PersistentList<T> list(T one) {
        return cons(one, PersistentList.<T>empty());
    }

    public static <T> PersistentList<T> list(T one, T two) {
        return cons(one, cons(two, PersistentList.<T>empty()));
    }

    public static <T> PersistentList<T> list(T one, T two, T three) {
        return cons(one, cons(two, cons(three, PersistentList.<T>empty())));
    }

    public static <T> PersistentList<T> list(T one, T two, T three, T four) {
        return cons(one, cons(two, cons(three, cons(four, PersistentList.<T>empty()))));
    }

    public static <T> PersistentList<T> list(T one, T two, T three, T four, T five) {
        return cons(one, cons(two, cons(three, cons(four, cons(five, PersistentList.<T>empty())))));
    }

    public static <T> PersistentList<T> list(T... values) {
        return list(sequence(values));
    }

    public static <T> PersistentList<T> list(Iterable<T> values) {
        return sequence(values).foldRight(PersistentList.<T>empty(), PersistentList.<T>cons().flip());
    }

    public abstract T head();

    public abstract PersistentList<T> tail();

    public abstract int size();

    public abstract boolean isEmpty();

    public PersistentList<T> cons(T head) {
        return cons(head, this);
    }

    public PersistentList<T> remove(T value) {
        return list(toSequence().filter(not(onlyOnce(is(value)))));
    }

    public Sequence<T> toSequence() {
        return sequence(this);
    }

    public PersistentList<T> removeAll(Iterable<T> values) {
        return list(toSequence().filter(not(in(set(values)))));
    }

    public static <T> Function2<PersistentList<T>, T, PersistentList<T>> cons() {
        return new Function2<PersistentList<T>, T, PersistentList<T>>() {
            @Override
            public PersistentList<T> call(PersistentList<T> ts, T t) throws Exception {
                return cons(t, ts);
            }
        };
    }

    public PersistentList<T> add(T value) {
        return list(toSequence().add(value));
    }

    public List<T> toList() {
        return toSequence().toList();
    }

    @Override
    public Iterator<T> iterator() {
        return new PersistentListIterator<T>(this);
    }

    private static class Empty<T> extends PersistentList<T> {
        private Empty() {
        }

        @Override
        public T head() {
            throw new NoSuchElementException();
        }

        @Override
        public PersistentList<T> tail() {
            throw new NoSuchElementException();
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
        private final PersistentList<T> tail;
        private final int size;

        private Node(T head, PersistentList<T> tail) {
            this.head = head;
            this.tail = tail;
            size = 1 + tail.size();
        }

        @Override
        public T head() {
            return head;
        }

        @Override
        public PersistentList<T> tail() {
            return tail;
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
            return String.format("%s::%s", head, tail);
        }

        @Override
        public int hashCode() {
            return 19 * head.hashCode() * tail.hashCode();
        }

        @Override
        public boolean equals(Object obj) {
            return obj instanceof Node ? ((Node) obj).head.equals(head) && ((Node) obj).tail.equals(tail) : false;
        }
    }

    private static class PersistentListIterator<T> extends ReadOnlyIterator<T> {
        private PersistentList<T> list;

        public PersistentListIterator(PersistentList<T> list) {
            this.list = list;
        }

        @Override
        public boolean hasNext() {
            return !list.isEmpty();
        }

        @Override
        public T next() {
            final T head = list.head();
            list = list.tail();
            return head;
        }
    }


}
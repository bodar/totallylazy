package com.googlecode.totallylazy;

import com.googlecode.totallylazy.iterators.EmptyIterator;
import com.googlecode.totallylazy.iterators.ReadOnlyListIterator;
import com.googlecode.totallylazy.iterators.StatefulIterator;

import java.util.AbstractList;
import java.util.AbstractSequentialList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

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

    public static <T> Cons<T> cons(T head, PersistentList<T> tail) {
        return new Cons<T>(head, tail);
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

    public abstract int size();

    public PersistentList<T> cons(T head) {
        return cons(head, this);
    }

    public PersistentList<T> remove(T value) {
        return list(sequence(this).filter(not(onlyOnce(is(value)))));
    }

    public PersistentList<T> removeAll(Iterable<T> values) {
        return list(sequence(this).filter(not(in(set(values)))));
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
        return list(sequence(this).add(value));
    }

    public List<T> toList(){
        return sequence(this).toList();
    }

    private static class Empty<T> extends PersistentList<T> {
        private Empty() {
        }

        @Override
        public int size() {
            return 0;
        }

        @Override
        public Iterator<T> iterator() {
            return new EmptyIterator<T>();
        }

        @Override
        public String toString() {
            return "[]";
        }
    }

    private static class Cons<T> extends PersistentList<T> {
        private final T head;
        private final PersistentList<T> tail;
        private final int size;

        private Cons(T head, PersistentList<T> tail) {
            this.head = head;
            this.tail = tail;
            size = 1 + tail.size();
        }

        @Override
        public int size() {
            return size;
        }

        @Override
        public Iterator<T> iterator() {
            return Iterators.flatten(sequence(new OneElementIterator<T>(head), tail.iterator()).iterator());
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
            return obj instanceof Cons ? ((Cons) obj).head.equals(head) && ((Cons) obj).tail.equals(tail) : false;
        }
    }

    private static class OneElementIterator<T> extends StatefulIterator<T> {
        private final T value;
        private boolean called;

        public OneElementIterator(T value) {
            this.value = value;
        }

        @Override
        protected T getNext() throws Exception {
            if (called) {
                return finished();
            }
            called = true;
            return value;
        }
    }

}
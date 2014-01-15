package com.googlecode.totallylazy.collections;

import com.googlecode.totallylazy.BiFunction;
import com.googlecode.totallylazy.Function;
import com.googlecode.totallylazy.Option;
import com.googlecode.totallylazy.Predicate;
import com.googlecode.totallylazy.Segment;
import com.googlecode.totallylazy.iterators.SegmentIterator;

import java.util.Iterator;
import java.util.NoSuchElementException;

import static com.googlecode.totallylazy.None.none;
import static com.googlecode.totallylazy.Option.some;
import static com.googlecode.totallylazy.Predicates.in;
import static com.googlecode.totallylazy.Predicates.not;
import static com.googlecode.totallylazy.Sequences.sequence;
import static com.googlecode.totallylazy.Sets.set;
import static com.googlecode.totallylazy.Unchecked.cast;

public abstract class LinkedList<T> extends AbstractList<T> implements PersistentList<T> {
    static final Empty EMPTY = new Empty();

    static <T> LinkedList<T> emptyList() {
        return cast(EMPTY);
    }

    static <T> LinkedList<T> cons(T head, PersistentList<T> tail) {
        return Node.node(head, tail);
    }

    @Override
    public PersistentList<T> empty() {
        return emptyList();
    }

    @Override
    public PersistentList<T> cons(T head) {
        return cons(head, this);
    }

    @Override
    public PersistentList<T> append(T value) {
        return PersistentList.constructors.list(toSequence().append(value));
    }

    @Override
    public Iterator<T> iterator() {
        return SegmentIterator.iterator(this);
    }

    private static class Empty<T> extends LinkedList<T> {
        private Empty() {
        }

        @Override
        public T head() {
            throw new NoSuchElementException();
        }

        @Override
        public Option<T> headOption() {
            return none();
        }

        @Override
        public PersistentList<T> tail() {
            throw new NoSuchElementException();
        }

        @Override
        public <C extends Segment<T>> C joinTo(C rest) {
            return rest;
        }

        @Override
        public PersistentList<T> delete(T value) {
            return this;
        }

        @Override
        public int size() {
            return 0;
        }

        @Override
        public <S> PersistentList<S> map(Function<? super T, ? extends S> callable) {
            return cast(this);
        }

        @Override
        public Option<T> find(Predicate<? super T> predicate) {
            return Option.none();
        }

        @Override
        public PersistentList<T> filter(Predicate<? super T> predicate) {
            return this;
        }

        @Override
        public <S> S fold(S seed, BiFunction<? super S, ? super T, ? extends S> callable) {
            return seed;
        }

        @Override
        public boolean contains(Object other) {
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

        @Override
        public T get(int i) throws IndexOutOfBoundsException {
            throw new IndexOutOfBoundsException();
        }

        @Override
        public int indexOf(Object t) {
            return -1;
        }
    }

    private static class Node<T> extends LinkedList<T> {
        private final T head;
        private final PersistentList<T> tail;
        private final int size;

        private Node(T head, PersistentList<T> tail) {
            this.head = head;
            this.tail = tail;
            size = 1 + tail.size();
        }

        private static <T> Node<T> node(T head, PersistentList<T> tail) {
            return new Node<T>(head, tail);
        }

        @Override
        public T head() {
            return head;
        }

        @Override
        public Option<T> headOption() {
            return some(head);
        }

        @Override
        public PersistentList<T> tail() {
            return tail;
        }

        @Override
        public <C extends Segment<T>> C joinTo(C rest) {
            return cast(tail.joinTo(rest).cons(head));
        }

        @Override
        public PersistentList<T> delete(T value) {
            if(useRecursion()){
                if(head.equals(value)) return tail;
                return cons(head, tail.delete(value));
            }
            ListZipper<T> zipper = ListZipper.zipper(this);
            while (!zipper.isBottom()){
                if(zipper.current().equals(value)) return zipper.delete().toList();
                zipper = zipper.next();
            }
            return this;
        }

        @Override
        public int size() {
            return size;
        }

        @Override
        public <S> PersistentList<S> map(Function<? super T, ? extends S> callable) {
            return node(callable.apply(head), tail().map(callable));
        }

        @Override
        public Option<T> find(Predicate<? super T> predicate) {
            return toSequence().find(predicate);
        }

        @Override
        public PersistentList<T> filter(Predicate<? super T> predicate) {
            if(useRecursion()) {
                if(predicate.matches(head)) return cons(head, tail().filter(predicate));
                return tail().filter(predicate);
            }
            return PersistentList.constructors.list(toSequence().filter(predicate));
        }

        @Override
        public <S> S fold(S seed, BiFunction<? super S, ? super T, ? extends S> callable) {
            return toSequence().fold(seed, callable);
        }

        protected boolean useRecursion() {
            return size < 1024;
        }

        @Override
        public boolean contains(Object other) {
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

        @Override
        public T get(int i) throws IndexOutOfBoundsException {
            if(i == 0) return head;
            return tail().get(i - 1);
        }

        @Override
        public int indexOf(Object t) {
            if(t.equals(head)) return 0;
            return 1 + tail.indexOf(t);
        }
    }
}
package com.googlecode.totallylazy;

import com.googlecode.totallylazy.iterators.SegmentIterator;

import java.util.Iterator;
import java.util.NoSuchElementException;

import static com.googlecode.totallylazy.Unchecked.cast;

public interface Segment<T> {
    boolean isEmpty();

    T head() throws NoSuchElementException;

    Segment<T> tail() throws NoSuchElementException;

    Segment<T> cons(T head);

    <C extends Segment<T>> C joinTo(C rest);

    class constructors {
        public static <T> Segment<T> emptySegment() {
            return new EmptySegment<T>();
        }

        public static <T> Segment<T> segment(final T head) {
            return segment(head, constructors.<T>emptySegment());
        }

        public static <T> OneSegment<T> segment(T head, Segment<T> empty) {
            return new OneSegment<T>(head, empty);
        }

    }

    class methods {
        public static <T, S extends Segment<T>> S cons(T head, S segment) {
            return cast(segment.cons(head));
        }

        public static <T, A extends Segment<T>, B extends Segment<T>> B joinTo(A source, B destination) {
            return cast(source.joinTo(destination));
        }

        public static <T> Sequence<T> sequence(final Segment<T> segments) {
            return new Sequence<T>() {
                @Override
                public Iterator<T> iterator() {
                    return new SegmentIterator<T>(segments);
                }
            };
        }
    }

    class functions {
        public static <T, Self extends Segment<T>> Function2<Self, T, Self> cons() {
            return new Function2<Self, T, Self>() {
                @Override
                public Self call(Self set, T t) throws Exception {
                    return cast(set.cons(t));
                }
            };
        }
    }

    class OneSegment<T> implements Segment<T> {
        private final T head;
        private final Segment<T> tail;

        public OneSegment(T head, Segment<T> tail) {
            this.head = head;
            this.tail = tail;
        }

        @Override
        public boolean isEmpty() {
            return false;
        }

        @Override
        public T head() throws NoSuchElementException {
            return head;
        }

        @Override
        public Segment<T> tail() throws NoSuchElementException {
            return tail;
        }

        @Override
        public Segment<T> cons(T head) {
            return constructors.segment(head, this);
        }

        @Override
        public <C extends Segment<T>> C joinTo(C rest) {
            return cast(tail().joinTo(rest).cons(head()));
        }
    }

    class EmptySegment<T> implements Segment<T> {
        @Override
        public boolean isEmpty() {
            return true;
        }

        @Override
        public T head() throws NoSuchElementException {
            throw new NoSuchElementException();
        }

        @Override
        public Segment<T> tail() throws NoSuchElementException {
            throw new NoSuchElementException();
        }

        @Override
        public Segment<T> cons(T head) {
            return constructors.segment((T) head);
        }

        @Override
        public <C extends Segment<T>> C joinTo(C rest) {
            return rest;
        }
    }

}

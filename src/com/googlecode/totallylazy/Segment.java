package com.googlecode.totallylazy;

import com.googlecode.totallylazy.iterators.SegmentIterator;
import com.googlecode.totallylazy.segments.AbstractSegment;
import com.googlecode.totallylazy.segments.CharacterSegment;

import java.util.Iterator;
import java.util.NoSuchElementException;

import static com.googlecode.totallylazy.Option.some;
import static com.googlecode.totallylazy.Unchecked.cast;

public interface Segment<T> {
    boolean isEmpty();

    T head() throws NoSuchElementException;

    Segment<T> tail() throws NoSuchElementException;

    Segment<T> cons(T head);

    <C extends Segment<T>> C joinTo(C rest);

    class constructors {
        public static <T> Segment<T> emptySegment(Class<T> aClass) {
            return new EmptySegment<T>();
        }

        public static <T> Segment<T> emptySegment() {
            return new EmptySegment<T>();
        }

        public static <T> Segment<T> segment(final T head) {
            return segment(head, constructors.<T>emptySegment());
        }

        public static <T> Segment<T> segment(T head, Segment<T> empty) {
            return ASegment.segment(head, empty);
        }

        public static <T> Segment<T> unique(T head, Segment<T> tail) {
            return segment(head, tail.head().equals(head) ? tail.tail() : tail);
        }

        public static Segment<Character> characters(CharSequence charSequence) {
            return CharacterSegment.characterSegment(charSequence);
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

        public static <T> Option<T> headOption(Segment<T> segment) {
            return segment.isEmpty() ? Option.<T>none() : some(segment.head());
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

    class ASegment<T> extends AbstractSegment<T> {
        private final T head;
        private final Segment<T> tail;

        private ASegment(T head, Segment<T> tail) {
            this.head = head;
            this.tail = tail;
        }

        static <T> ASegment<T> segment(T head, Segment<T> tail) {
            return new ASegment<T>(head, tail);
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

    }

    class EmptySegment<T> extends AbstractSegment<T> {
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
        public <C extends Segment<T>> C joinTo(C rest) {
            return rest;
        }
    }

}

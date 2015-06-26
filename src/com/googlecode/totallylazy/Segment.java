package com.googlecode.totallylazy;

import com.googlecode.totallylazy.functions.Curried2;
import com.googlecode.totallylazy.functions.Function1;
import com.googlecode.totallylazy.functions.Unary;
import com.googlecode.totallylazy.iterators.SegmentIterator;
import com.googlecode.totallylazy.segments.AbstractSegment;
import com.googlecode.totallylazy.segments.CharacterSegment;

import java.io.Reader;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.concurrent.Callable;

import static com.googlecode.totallylazy.Option.none;
import static com.googlecode.totallylazy.Option.some;
import static com.googlecode.totallylazy.Unchecked.cast;

public interface Segment<T> {
    Option<T> headOption();

    Segment<T> tail() throws NoSuchElementException;

    default boolean isEmpty() {
        return headOption().isEmpty();
    }

    default T head() throws NoSuchElementException {
        return headOption().get();
    }

    default Segment<T> empty() {
        return constructors.emptySegment();
    }

    default Segment<T> cons(T head) {
        return constructors.cons(head, this);
    }

    default <C extends Segment<T>> C joinTo(C rest) {
        return cast(tail().joinTo(rest).cons(head()));
    }

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

        public static <T> Segment<T> segment(T head, Segment<T> tail) {
            return ASegment.segment(head, tail);
        }

        public static <T> Segment<T> cons(T head, Segment<T> tail) {
            return segment(head, tail);
        }

        public static <T> Segment<T> unique(T head, Segment<T> tail) {
            return segment(head, tail.head().equals(head) ? tail.tail() : tail);
        }

        public static Segment<Character> characters(CharSequence charSequence) {
            return CharacterSegment.characterSegment(charSequence);
        }

        public static Computation<Character> characters(final Reader reader) {
            return Computation.computation1((Callable<Option<Character>>) () -> {
                int read = reader.read();
                if (read == -1) return none();
                return some((char) read);
            }, (Character character) -> characters(reader));
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
                    return SegmentIterator.iterator(segments);
                }
            };
        }

        public static <T> Option<T> headOption(Segment<T> segment) {
            return segment.isEmpty() ? Option.<T>none() : some(segment.head());
        }

        public static boolean equalTo(Segment<?> a, Segment<?> b) {
            return Sequences.equalTo(methods.sequence(a), methods.sequence(b));
        }

        public static String toString(Segment<?> segment, String separator) {
            return Iterators.toString(SegmentIterator.iterator(segment), separator);
        }
    }

    class functions {
        public static <T, Self extends Segment<T>> Curried2<Self, T, Self> cons() {
            return (set, t) -> cast(set.cons(t));
        }

        public static <T, Self extends Segment<T>> Unary<Self> tail() {
            return segment -> cast(segment.tail());
        }

        public static <T, Self extends Segment<T>> Function1<Self, Self> cons(T t) {
            return functions.<T, Self>cons().flip().apply(t);
        }

        public static Function1<String, Segment<Character>> characters() {
            return constructors::characters;
        }

        public static <T> Function1<Segment<T>, Option<T>> headOption() {
            return Segment<T>::headOption;
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

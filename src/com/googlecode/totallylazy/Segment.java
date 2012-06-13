package com.googlecode.totallylazy;

import java.util.NoSuchElementException;

import static com.googlecode.totallylazy.Unchecked.cast;

public interface Segment<T> {
    boolean isEmpty();

    T head() throws NoSuchElementException;

    Segment<T> tail() throws NoSuchElementException;

    Segment<T> cons(T head);

    <C extends Segment<T>> C joinTo(C rest);

    class methods {
        public static <T, S extends Segment<T>> S cons(T head, S segment){
            return cast(segment.cons(head));
        }

        public static <T, A extends Segment<T>, B extends Segment<T>> B joinTo(A source, B destination){
            return cast(source.joinTo(destination));
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
}

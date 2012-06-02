package com.googlecode.totallylazy;

import java.util.NoSuchElementException;

public interface Segment<T, Self extends Segment<T, Self>> {
    boolean isEmpty();

    T head() throws NoSuchElementException;

    Self tail() throws NoSuchElementException;

    Self cons(T head);

    <C extends Segment<T, C>> C joinTo(C rest);

    class functions {
        public static <T, Self extends Segment<T, Self>> Function2<Self, T, Self> cons() {
            return new Function2<Self, T, Self>() {
                @Override
                public Self call(Self set, T t) throws Exception {
                    return set.cons(t);
                }
            };
        }
    }
}

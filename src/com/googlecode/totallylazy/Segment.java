package com.googlecode.totallylazy;

import java.util.NoSuchElementException;

public interface Segment<T, Self extends Segment<T, Self>> {
    boolean isEmpty();

    T head() throws NoSuchElementException;

    Self tail() throws NoSuchElementException;
}

package com.googlecode.totallylazy;

import java.util.NoSuchElementException;

public interface Deconstructable<T, Self extends Deconstructable<T, Self>> {
    boolean isEmpty();
    T head() throws NoSuchElementException;
    Self tail() throws NoSuchElementException;
}

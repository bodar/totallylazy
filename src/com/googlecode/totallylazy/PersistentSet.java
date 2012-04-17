package com.googlecode.totallylazy;

public interface PersistentSet<T> extends Iterable<T>, Constructable<T, PersistentSet<T>>, Segment<T, PersistentSet<T>>, Container<T> {
    PersistentList<T> persistentList();
}

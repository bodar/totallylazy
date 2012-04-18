package com.googlecode.totallylazy.collections;

import com.googlecode.totallylazy.Constructable;
import com.googlecode.totallylazy.Container;
import com.googlecode.totallylazy.Segment;

public interface PersistentSet<T> extends Iterable<T>, Constructable<T, PersistentSet<T>>, Segment<T, PersistentSet<T>>, Container<T> {
    PersistentList<T> persistentList();
}

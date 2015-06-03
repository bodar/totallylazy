package com.googlecode.totallylazy.xml.streaming;

import com.googlecode.totallylazy.collections.PersistentMap;

import static com.googlecode.totallylazy.collections.PersistentMap.constructors.emptyMap;

public interface Node {
    default String name() { throw new UnsupportedOperationException(); }

    default String text() { throw new UnsupportedOperationException(); }

    default PersistentMap<String, String> attributes() { return emptyMap(); }

    default boolean isText() { return false; }

    default boolean isElement() { return false; }

}

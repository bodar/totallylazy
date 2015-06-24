package com.googlecode.totallylazy.collections;

interface Selection {
    PersistentMap<String, Object> select(PersistentMap<String, Object> source, PersistentMap<String, Object> destination);
}

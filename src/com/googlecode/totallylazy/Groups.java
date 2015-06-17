package com.googlecode.totallylazy;

public class Groups {
    public static <K> Function1<Group<K, ?>, K> groupKey(Class<K> keyType) {
        return groupKey();
    }

    public static <K> Function1<Group<K, ?>, K> groupKey() {
        return group -> group.key();
    }
}

package com.googlecode.totallylazy;

import com.googlecode.totallylazy.functions.Function1;

public class Groups {
    public static <K> Function1<Group<K, ?>, K> groupKey(Class<K> keyType) {
        return groupKey();
    }

    public static <K> Function1<Group<K, ?>, K> groupKey() {
        return group -> group.key();
    }
}

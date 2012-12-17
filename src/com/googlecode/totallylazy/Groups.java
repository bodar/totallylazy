package com.googlecode.totallylazy;

public class Groups {
    public static <K> Function1<Group<K, ?>, K> groupKey(Class<K> keyType) {
        return groupKey();
    }

    public static <K> Function1<Group<K, ?>, K> groupKey() {
        return new Function1<Group<K, ?>, K>() {
            @Override
            public K call(Group<K, ?> group) throws Exception {
                return group.key();
            }
        };
    }
}

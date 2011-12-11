package com.googlecode.totallylazy;

public class Groups {
    public static <K> Callable1<Group<K, ?>, K> groupKey(Class<K> keyType) {
        return groupKey();
    }

    public static <K> Callable1<Group<K, ?>, K> groupKey() {
        return new Callable1<Group<K, ?>, K>() {
            @Override
            public K call(Group<K, ?> group) throws Exception {
                return group.key();
            }
        };
    }
}

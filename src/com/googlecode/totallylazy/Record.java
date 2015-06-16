package com.googlecode.totallylazy;


import java.lang.reflect.Field;
import java.util.AbstractMap;
import java.util.Set;

import static com.googlecode.totallylazy.Fields.nonSyntheticFields;
import static com.googlecode.totallylazy.Pair.pair;
import static com.googlecode.totallylazy.Sets.set;
import static java.util.Collections.unmodifiableSet;

public abstract class Record extends AbstractMap<String, Object> {
    @Override
    public Set<Entry<String, Object>> entrySet() {
        return unmodifiableSet(set(nonSyntheticFields(getClass()).map(new Function1<Field, Entry<String, Object>>() {
            @Override
            public Entry<String, Object> call(Field f) throws Exception {return pair(f.getName(), Fields.get(f, Record.this));}
        })));
    }
}

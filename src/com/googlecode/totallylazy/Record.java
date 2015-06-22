package com.googlecode.totallylazy;


import com.googlecode.totallylazy.reflection.Fields;

import java.util.AbstractMap;
import java.util.Set;

import static com.googlecode.totallylazy.reflection.Fields.nonSyntheticFields;
import static com.googlecode.totallylazy.Pair.pair;
import static com.googlecode.totallylazy.Sets.set;
import static java.util.Collections.unmodifiableSet;

public abstract class Record extends AbstractMap<String, Object> {
    @Override
    public Set<Entry<String, Object>> entrySet() {
        return unmodifiableSet(set(nonSyntheticFields(getClass()).map(f -> pair(f.getName(), Fields.get(f, Record.this)))));
    }
}

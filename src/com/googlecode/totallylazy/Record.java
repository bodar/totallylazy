package com.googlecode.totallylazy;


import com.googlecode.totallylazy.json.Json;

import java.util.AbstractMap;
import java.util.Set;

import static com.googlecode.totallylazy.Fields.nonSyntheticFields;
import static com.googlecode.totallylazy.Pair.pair;
import static java.util.Collections.unmodifiableSet;

public abstract class Record extends AbstractMap<String, Object> {
    @Override
    public Set<Entry<String, Object>> entrySet() {
        return unmodifiableSet(Sets.<Entry<String, Object>>set(nonSyntheticFields(getClass()).map(f -> pair(f.getName(), Fields.get(f, this)))));
    }

    @Override
    public String toString() {
        return Json.json(this);
    }
}

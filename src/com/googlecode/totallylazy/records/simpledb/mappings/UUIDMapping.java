package com.googlecode.totallylazy.records.simpledb.mappings;

import java.util.UUID;

public class UUIDMapping implements Mapping<UUID>{
    public UUID toValue(String value) {
        return UUID.fromString(value);
    }

    public String toString(UUID value) {
        return value.toString();
    }
}

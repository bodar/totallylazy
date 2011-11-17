package com.googlecode.totallylazy.records.lucene.mappings;

import org.apache.lucene.document.Field;

import java.text.ParseException;
import java.util.UUID;

public class UUIDMapping extends AbstractStringMapping<UUID> {
    public UUIDMapping() {
        super(Field.Index.NOT_ANALYZED);
    }

    @Override
    protected String toString(UUID value) {
        return value.toString();
    }

    @Override
    protected UUID fromString(String value) throws ParseException {
        return UUID.fromString(value);
    }
}

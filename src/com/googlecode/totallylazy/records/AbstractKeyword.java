package com.googlecode.totallylazy.records;

public abstract class AbstractKeyword<T> implements Keyword<T> {
    private final Record metadata = MapRecord.record();

    @Override
    public boolean equals(Object other) {
        return other instanceof Keyword && Keywords.equalto(this, (Keyword) other);
    }

    @Override
    public int hashCode() {
        return name().toLowerCase().hashCode();
    }

    public T call(Record record) throws Exception {
        return record.get(this);
    }

    @Override
    public String toString() {
        return name();
    }

    public Record metadata() {
        return metadata;
    }
}

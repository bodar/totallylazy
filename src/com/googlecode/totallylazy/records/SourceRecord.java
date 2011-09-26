package com.googlecode.totallylazy.records;

import com.googlecode.totallylazy.Value;

public class SourceRecord<T> extends MapRecord implements Value<T>{
    private final T value;

    public SourceRecord(T value) {
        this.value = value;
    }

    public static <T> SourceRecord<T> record(T value){
        return new SourceRecord<T>(value);
    }

    public T value() {
        return value;
    }
}

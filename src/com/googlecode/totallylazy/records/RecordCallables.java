package com.googlecode.totallylazy.records;

import com.googlecode.totallylazy.Callable1;
import com.googlecode.totallylazy.Callable2;
import com.googlecode.totallylazy.Pair;

public class RecordCallables {
    @SuppressWarnings({"unchecked"})
    public static Callable2<? super Record, ? super Pair<Keyword, Object>, Record> updateValues() {
        return new Callable2<Record, Pair<Keyword, Object>, Record>() {
            public Record call(Record record, Pair<Keyword, Object> field) throws Exception {
                return record.set(field.first(), field.second());
            }
        };
    }

    public static Callable1<? super Record, Record> merge(final Record other) {
        return new Callable1<Record, Record>() {
            public Record call(Record record) throws Exception {
                return other.fields().fold(record, updateValues());
            }
        };
    }
}

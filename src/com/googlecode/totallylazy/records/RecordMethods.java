package com.googlecode.totallylazy.records;

import com.googlecode.totallylazy.Callable1;
import com.googlecode.totallylazy.Callable2;
import com.googlecode.totallylazy.Pair;
import com.googlecode.totallylazy.Sequence;
import com.googlecode.totallylazy.Sequences;

import static com.googlecode.totallylazy.Callables.first;
import static com.googlecode.totallylazy.Predicates.*;
import static com.googlecode.totallylazy.Strings.equalIgnoringCase;
import static com.googlecode.totallylazy.records.Keywords.keyword;
import static com.googlecode.totallylazy.records.Keywords.name;

public class RecordMethods {
    @SuppressWarnings({"unchecked"})
    public static Callable2<? super Record, ? super Pair<Keyword, Object>, Record> updateValues() {
        return new Callable2<Record, Pair<Keyword, Object>, Record>() {
            public Record call(Record record, Pair<Keyword, Object> field) throws Exception {
                return record.set(field.first(), field.second());
            }
        };
    }

    public static Callable1<? super Record, Record> merge(final Record other) {
        return merge(other.fields());
    }

    public static Callable1<? super Record, Record> merge(final Sequence<Pair<Keyword, Object>> fields) {
        return new Callable1<Record, Record>() {
            public Record call(Record record) throws Exception {
                return fields.fold(record, updateValues());
            }
        };
    }

    public static Keyword getKeyword(String name, Sequence<Keyword> definitions) {
        return definitions.find(where(name(), equalIgnoringCase(name))).getOrElse(keyword(name));
    }

    public static Callable1<? super Record, Sequence<Object>> getValuesFor(final Sequence<Keyword> fields) {
        return new Callable1<Record, Sequence<Object>>() {
            public Sequence<Object> call(Record record) throws Exception {
                return record.getValuesFor(fields);
            }
        };
    }

    public static Record filter(Record original, Keyword... fields) {
        return filter(original, Sequences.sequence(fields));
    }

    public static Record filter(Record original, Sequence<Keyword> fields) {
        return record(original.fields().filter(where(first(Keyword.class), is(in(fields)))));
    }

    public static Record record(final Pair<Keyword, Object>... fields) {
        return Sequences.sequence(fields).fold(new MapRecord(), updateValues());
    }

    public static Record record(final Sequence<Pair<Keyword, Object>> fields) {
        return fields.fold(new MapRecord(), updateValues());
    }
}

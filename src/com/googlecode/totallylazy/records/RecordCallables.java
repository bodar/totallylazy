package com.googlecode.totallylazy.records;

import com.googlecode.totallylazy.Callable1;
import com.googlecode.totallylazy.Callable2;
import com.googlecode.totallylazy.Pair;
import com.googlecode.totallylazy.Sequence;

import static com.googlecode.totallylazy.Predicates.where;
import static com.googlecode.totallylazy.Strings.equalIgnoringCase;
import static com.googlecode.totallylazy.records.Keywords.keyword;
import static com.googlecode.totallylazy.records.Keywords.name;

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
}

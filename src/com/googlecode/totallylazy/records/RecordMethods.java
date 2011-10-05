package com.googlecode.totallylazy.records;

import com.googlecode.totallylazy.Callable1;
import com.googlecode.totallylazy.Callable2;
import com.googlecode.totallylazy.Callables;
import com.googlecode.totallylazy.Pair;
import com.googlecode.totallylazy.Predicate;
import com.googlecode.totallylazy.Sequence;
import com.googlecode.totallylazy.Sequences;

import java.util.Map;

import static com.googlecode.totallylazy.Callables.asString;
import static com.googlecode.totallylazy.Callables.first;
import static com.googlecode.totallylazy.Maps.map;
import static com.googlecode.totallylazy.Predicates.in;
import static com.googlecode.totallylazy.Predicates.is;
import static com.googlecode.totallylazy.Predicates.where;
import static com.googlecode.totallylazy.Sequences.sequence;
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

    public static Sequence<Pair<? extends Predicate<? super Record>, Record>> update(final Callable1<Record, Predicate<? super Record>> callable, final Record... records) {
        return update(callable, sequence(records));
    }

    public static Sequence<Pair<? extends Predicate<? super Record>, Record>> update(final Callable1<Record, Predicate<? super Record>> callable, final Sequence<Record> records) {
        return records.map(toPair(callable));
    }

    public static Callable1<Record, Pair<? extends Predicate<? super Record>, Record>> toPair(final Callable1<Record, Predicate<? super Record>> callable) {
        return new Callable1<Record, Pair<? extends Predicate<? super Record>, Record>>() {
            public Pair<? extends Predicate<? super Record>, Record> call(Record record) throws Exception {
                return Pair.pair(callable.call(record), record);
            }
        };
    }

    public static Callable1<? super Record, Map<String, Object>> asMap() {
        return new Callable1<Record, Map<String, Object>>() {
            public Map<String, Object> call(Record record) throws Exception {
                return toMap(record);
            }
        };
    }

    public static Map<String, Object> toMap(Record record) {
        return map(record.fields().map(Callables.<Keyword, Object, String>first(asString(Keyword.class))));
    }

    public static Callable2<? super Map<String, Object>, ? super Pair<Keyword, Object>, Map<String, Object>> intoMap() {
        return new Callable2<Map<String, Object>, Pair<Keyword, Object>, Map<String, Object>>() {
            public Map<String, Object> call(Map<String, Object> map, Pair<Keyword, Object> pair) throws Exception {
                map.put(pair.first().toString(), pair.second());
                return map;
            }
        };
    }
}

package com.googlecode.totallylazy.records;

import com.googlecode.totallylazy.Callable1;
import com.googlecode.totallylazy.Predicate;
import com.googlecode.totallylazy.Sequence;

import static com.googlecode.totallylazy.records.RecordMethods.merge;

public class Join implements Callable1<Record, Iterable<Record>> {
    private final Sequence<Record> records;
    private final Callable1<Record, Predicate<? super Record>> using;

    public Join(Sequence<Record> records, Callable1<Record, Predicate<? super Record>> using) {
        this.records = records;
        this.using = using;
    }

    public Iterable<Record> call(Record record) throws Exception {
        return records.filter(using.call(record)).map(merge(record));
    }

    public static Callable1<? super Record,Iterable<Record>> join(final Sequence<Record> records,
                                                                  final Callable1<Record, Predicate<? super Record>> using) {
        return new Join(records, using);
    }
}
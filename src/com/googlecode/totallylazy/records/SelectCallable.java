package com.googlecode.totallylazy.records;

import com.googlecode.totallylazy.Callable1;
import com.googlecode.totallylazy.Sequence;

import static com.googlecode.totallylazy.Sequences.sequence;

public class SelectCallable implements Callable1<Record, Record> {
    private final Sequence<Keyword> keywords;

    public SelectCallable(Sequence<Keyword> keywords) {
        this.keywords = keywords;
    }

    public Record call(Record record) throws Exception {
        Record result = new MapRecord();
        for (Keyword keyword : keywords) {
            result.set(keyword, record.get(keyword));
        }
        return result;
    }

    public Sequence<Keyword> keywords() {
        return keywords;
    }

    public static Callable1<? super Record, Record> select(final Keyword... keywords) {
        return new SelectCallable(sequence(keywords));
    }

    public static Callable1<? super Record, Record> select(final Sequence<Keyword> keywords) {
        return new SelectCallable(keywords);
    }

}

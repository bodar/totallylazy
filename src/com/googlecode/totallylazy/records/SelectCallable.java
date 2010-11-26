package com.googlecode.totallylazy.records;

import com.googlecode.totallylazy.Callable1;

public class SelectCallable implements Callable1<Record, Record> {
    private final Keyword[] keywords;

    public SelectCallable(Keyword... keywords) {
        this.keywords = keywords;
    }

    public Record call(Record record) throws Exception {
        Record result = new MapRecord();
        for (Keyword keyword : keywords) {
            result.set(keyword, record.get(keyword));
        }
        return result;
    }

    public Keyword[] keywords() {
        return keywords;
    }

    public static Callable1<? super Record, Record> select(final Keyword... keywords) {
        return new SelectCallable(keywords);
    }

}

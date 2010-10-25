package com.googlecode.totallylazy.sql;

import com.googlecode.totallylazy.Callable1;
import com.googlecode.totallylazy.sql.Keyword;
import com.googlecode.totallylazy.sql.MapRecord;
import com.googlecode.totallylazy.sql.Record;

public class KeywordsCallable implements Callable1<Record, Record> {
    private final Keyword[] keywords;

    public KeywordsCallable(Keyword... keywords) {
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
}

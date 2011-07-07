package com.googlecode.totallylazy.records.lucene;

import com.googlecode.totallylazy.Callable1;
import com.googlecode.totallylazy.Callable2;
import com.googlecode.totallylazy.Pair;
import com.googlecode.totallylazy.records.Keyword;
import com.googlecode.totallylazy.records.Record;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Fieldable;

import static com.googlecode.totallylazy.Pair.pair;
import static com.googlecode.totallylazy.Predicates.notNullValue;
import static com.googlecode.totallylazy.records.Keyword.keyword;

public class Lucene {
    public static final Keyword RECORD_KEY = keyword("totallylazy.record.name");



}

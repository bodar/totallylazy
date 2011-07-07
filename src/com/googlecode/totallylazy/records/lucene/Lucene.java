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

    public static Callable1<? super Record, Document> asDocument(final Keyword recordName) {
        return new Callable1<Record, Document>() {
            public Document call(Record record) throws Exception {
                return record.fields().
                        add(pair(RECORD_KEY, (Object) recordName)).
                        map(asField()).
                        filter(notNullValue()).
                        fold(new Document(), intoFields());
            }
        };
    }

    public static Callable2<? super Document, ? super Fieldable, Document> intoFields() {
        return new Callable2<Document, Fieldable, Document>() {
            public Document call(Document document, Fieldable fieldable) throws Exception {
                document.add(fieldable);
                return document;
            }
        };
    }

    public static Callable1<? super Pair<Keyword, Object>, Fieldable> asField() {
        return new Callable1<Pair<Keyword, Object>, Fieldable>() {
            public Fieldable call(Pair<Keyword, Object> pair) throws Exception {
                return pair.second() == null ? null :
                        new Field(pair.first().toString(), pair.second().toString(), Field.Store.YES, Field.Index.NOT_ANALYZED);
            }
        };
    }

}

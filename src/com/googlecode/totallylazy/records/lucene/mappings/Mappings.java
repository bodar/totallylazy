package com.googlecode.totallylazy.records.lucene.mappings;

import com.googlecode.totallylazy.Callable1;
import com.googlecode.totallylazy.Callable2;
import com.googlecode.totallylazy.Callables;
import com.googlecode.totallylazy.Pair;
import com.googlecode.totallylazy.Sequence;
import com.googlecode.totallylazy.records.Keyword;
import com.googlecode.totallylazy.records.MapRecord;
import com.googlecode.totallylazy.records.Record;
import com.googlecode.totallylazy.records.RecordCallables;
import com.googlecode.totallylazy.records.lucene.Lucene;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Fieldable;

import java.net.URI;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.googlecode.totallylazy.Pair.pair;
import static com.googlecode.totallylazy.Predicates.is;
import static com.googlecode.totallylazy.Predicates.not;
import static com.googlecode.totallylazy.Predicates.notNullValue;
import static com.googlecode.totallylazy.Predicates.where;
import static com.googlecode.totallylazy.Sequences.sequence;
import static com.googlecode.totallylazy.records.RecordCallables.updateValues;

public class Mappings {
    private final Map<Class, Mapping<Object>> map = new HashMap<Class, Mapping<Object>>();

    public Mappings() {
        add(Date.class, new DateMapping());
        add(Integer.class, new IntegerMapping());
        add(Long.class, new LongMapping());
        add(String.class, new StringMapping());
        add(URI.class, new UriMapping());
        add(Object.class, new ObjectMapping());
    }

    public <T> void add(final Class<T> type, final Mapping<T> mapping) {
        map.put(type, (Mapping<Object>) mapping);
    }

    public Mapping<Object> get(final Class aClass) {
        if(!map.containsKey(aClass)) {
            return map.get(Object.class);
        }
        return map.get(aClass);
    }

    public Callable1<? super Document, Record> asRecord(final Sequence<Keyword> definitions) {
        return new Callable1<Document, Record>() {
            public Record call(Document document) throws Exception {
                return sequence(document.getFields()).
                        map(asPair(definitions)).
                        filter(where(Callables.first(Keyword.class), is(not(Lucene.RECORD_KEY)))).
                        fold(new MapRecord(), updateValues());
            }
        };
    }

    public Callable1<? super Fieldable, Pair<Keyword, Object>> asPair(final Sequence<Keyword> definitions) {
        return new Callable1<Fieldable, Pair<Keyword, Object>>() {
            public Pair<Keyword, Object> call(Fieldable fieldable) throws Exception {
                String name = fieldable.name();
                Keyword keyword = RecordCallables.getKeyword(name, definitions);
                return pair(keyword, get(keyword.forClass()).toValue(fieldable));
            }
        };
    }

    public Callable1<? super Pair<Keyword, Object>, Fieldable> asField(final Sequence<Keyword> definitions) {
        return new Callable1<Pair<Keyword, Object>, Fieldable>() {
            public Fieldable call(Pair<Keyword, Object> pair) throws Exception {
                if (pair.second() == null) {
                    return null;
                }

                String name = pair.first().value();
                Keyword keyword = RecordCallables.getKeyword(name, definitions);
                return get(keyword.forClass()).toField(name, pair.second());
            }
        };
    }

    public Callable1<? super Record, Document> asDocument(final Keyword recordName, final Sequence<Keyword> definitions) {
        return new Callable1<Record, Document>() {
            public Document call(Record record) throws Exception {
                return record.fields().
                        add(pair(Lucene.RECORD_KEY, (Object) recordName)).
                        map(asField(definitions)).
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



}

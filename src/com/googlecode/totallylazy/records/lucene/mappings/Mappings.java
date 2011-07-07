package com.googlecode.totallylazy.records.lucene.mappings;

import com.googlecode.totallylazy.Callable1;
import com.googlecode.totallylazy.Callable2;
import com.googlecode.totallylazy.Pair;
import com.googlecode.totallylazy.Sequence;
import com.googlecode.totallylazy.records.Keyword;
import com.googlecode.totallylazy.records.Record;
import com.googlecode.totallylazy.records.lucene.Lucene;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Fieldable;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.googlecode.totallylazy.Pair.pair;
import static com.googlecode.totallylazy.Predicates.notNullValue;
import static com.googlecode.totallylazy.Predicates.where;
import static com.googlecode.totallylazy.Strings.equalIgnoringCase;
import static com.googlecode.totallylazy.numbers.Numbers.increment;
import static com.googlecode.totallylazy.records.Keyword.keyword;
import static com.googlecode.totallylazy.records.Keywords.name;

public class Mappings {
    private final Map<Class, Mapping<Object>> map = new HashMap<Class, Mapping<Object>>();

    public Mappings() {
        add(Date.class, new DateMapping());
        add(Integer.class, new IntegerMapping());
        add(Long.class, new LongMapping());
        add(String.class, new StringMapping());
        add(Object.class, new ObjectMapping());
    }

    public <T> void add(final Class<T> type, final Mapping<T> mapping) {
        map.put(type, (Mapping<Object>) mapping);
    }

    public Callable1<? super Fieldable, Pair<Keyword, Object>> asPair(final Sequence<Keyword> definitions) {
        return new Callable1<Fieldable, Pair<Keyword, Object>>() {
            public Pair<Keyword, Object> call(Fieldable fieldable) throws Exception {
                String name = fieldable.name();
                Keyword keyword = getKeyword(name, definitions);
                return pair(keyword, map.get(keyword.forClass()).toValue(fieldable));
            }
        };
    }

    private Keyword getKeyword(String name, Sequence<Keyword> definitions) {
        return definitions.find(where(name(), equalIgnoringCase(name))).getOrElse(keyword(name));
    }

    public Callable1<? super Pair<Keyword, Object>, Fieldable> asField(final Sequence<Keyword> definitions) {
        return new Callable1<Pair<Keyword, Object>, Fieldable>() {
            public Fieldable call(Pair<Keyword, Object> pair) throws Exception {
                if (pair.second() == null) {
                    return null;
                }

                String name = pair.first().toString();
                Keyword keyword = getKeyword(name, definitions);
                return map.get(keyword.forClass()).toField(name, pair.second());
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

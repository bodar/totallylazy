package com.googlecode.totallylazy.records.simpledb.mappings;

import com.amazonaws.services.simpledb.model.Attribute;
import com.amazonaws.services.simpledb.model.Item;
import com.amazonaws.services.simpledb.model.ReplaceableAttribute;
import com.amazonaws.services.simpledb.model.ReplaceableItem;
import com.googlecode.totallylazy.Callable1;
import com.googlecode.totallylazy.Callable2;
import com.googlecode.totallylazy.Pair;
import com.googlecode.totallylazy.Sequence;
import com.googlecode.totallylazy.records.Keyword;
import com.googlecode.totallylazy.records.Record;
import com.googlecode.totallylazy.records.RecordMethods;

import java.net.URI;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static com.googlecode.totallylazy.Callables.second;
import static com.googlecode.totallylazy.Predicates.is;
import static com.googlecode.totallylazy.Predicates.notNullValue;
import static com.googlecode.totallylazy.Predicates.where;
import static com.googlecode.totallylazy.Sequences.sequence;
import static com.googlecode.totallylazy.records.SourceRecord.record;

public class Mappings {
    private final Map<Class, Mapping<Object>> map = new HashMap<Class, Mapping<Object>>();

    public Mappings() {
        add(Date.class, new DateMapping());
        add(Integer.class, new IntegerMapping());
        add(Long.class, new LongMapping());
        add(URI.class, new UriMapping());
        add(Boolean.class, new BooleanMapping());
        add(UUID.class, new UUIDMapping());
        add(Object.class, new ObjectMapping());
    }

    @SuppressWarnings("unchecked")
    public <T> Mappings add(final Class<T> type, final Mapping<T> mapping) {
        map.put(type, (Mapping<Object>) mapping);
        return this;
    }

    public Mapping<Object> get(final Class aClass) {
        if(!map.containsKey(aClass)) {
            return map.get(Object.class);
        }
        return map.get(aClass);
    }

    public String toString(final Class aClass, Object value){
        try {
            return value == null ? null : get(aClass).toString(value);
        } catch (Exception e) {
            throw new UnsupportedOperationException();
        }
    }

    @SuppressWarnings("unchecked")
    public <T> T toValue(final Class<T> aClass, String value){
        try {
            return value == null ? null : (T) get(aClass).toValue(value);
        } catch (Exception e) {
            throw new UnsupportedOperationException();
        }
    }

    public Callable1<? super Item, Record> asRecord(final Sequence<Keyword> definitions) {
        return new Callable1<Item, Record>() {
            public Record call(Item item) throws Exception {
                return sequence(item.getAttributes()).fold(record(item), asField(definitions));
            }
        };
    }

    @SuppressWarnings("unchecked")
    public Callable2<? super Record, ? super Attribute, Record> asField(final Sequence<Keyword> definitions) {
        return new Callable2<Record, Attribute, Record>() {
            public Record call(Record mapRecord, Attribute attribute) throws Exception {
                Keyword keyword = RecordMethods.getKeyword(attribute.getName(), definitions);
                return mapRecord.set(keyword, toValue(keyword.forClass(), attribute.getValue()));
            }
        };
    }

    public Callable1<? super Record, ReplaceableItem> toReplaceableItem() {
        return new Callable1<Record, ReplaceableItem>() {
            public ReplaceableItem call(Record record) throws Exception {
                return new ReplaceableItem(UUID.randomUUID().toString(), record.fields().
                        filter(where(second(Object.class), is(notNullValue()))).
                        map(asAttribute()).toList());
            }
        };
    }

    public Callable1<? super Pair<Keyword, Object>, ReplaceableAttribute> asAttribute() {
        return new Callable1<Pair<Keyword, Object>, ReplaceableAttribute>() {
            public ReplaceableAttribute call(Pair<Keyword, Object> pair) throws Exception {
                Keyword keyword = pair.first();
                Object value = pair.second();
                return new ReplaceableAttribute(keyword.name(), Mappings.this.toString(keyword.forClass(), value), true);
            }
        };
    }


}
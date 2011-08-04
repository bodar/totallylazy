package com.googlecode.totallylazy.records.simpledb;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.services.simpledb.AmazonSimpleDB;
import com.amazonaws.services.simpledb.AmazonSimpleDBClient;
import com.amazonaws.services.simpledb.model.Attribute;
import com.amazonaws.services.simpledb.model.BatchPutAttributesRequest;
import com.amazonaws.services.simpledb.model.CreateDomainRequest;
import com.amazonaws.services.simpledb.model.Item;
import com.amazonaws.services.simpledb.model.ReplaceableAttribute;
import com.amazonaws.services.simpledb.model.ReplaceableItem;
import com.amazonaws.services.simpledb.model.SelectRequest;
import com.amazonaws.services.simpledb.model.SelectResult;
import com.googlecode.totallylazy.Callable1;
import com.googlecode.totallylazy.Callable2;
import com.googlecode.totallylazy.Pair;
import com.googlecode.totallylazy.Predicate;
import com.googlecode.totallylazy.Sequence;
import com.googlecode.totallylazy.Sequences;
import com.googlecode.totallylazy.records.AbstractRecords;
import com.googlecode.totallylazy.records.Keyword;
import com.googlecode.totallylazy.records.Keywords;
import com.googlecode.totallylazy.records.MapRecord;
import com.googlecode.totallylazy.records.Record;

import java.util.UUID;

public class SimpleDBRecords extends AbstractRecords {
    private final AmazonSimpleDB sdb;

    public SimpleDBRecords(final AWSCredentials awsCredentials) {
        sdb = new AmazonSimpleDBClient(awsCredentials, new ClientConfiguration());
    }

    @Override
    public void define(Keyword recordName, Keyword<?>... fields) {
        super.define(recordName, fields);
        sdb.createDomain(new CreateDomainRequest(recordName.name()));
    }

    public Sequence<Record> get(Keyword recordName) {
        SelectResult select = sdb.select(new SelectRequest("select * from `" + recordName.name() + "`"));
        return Sequences.sequence(select.getItems()).map(asRecord());
    }

    private Callable1<? super Item, Record> asRecord() {
        return new Callable1<Item, Record>() {
            public Record call(Item item) throws Exception {
                return Sequences.sequence(item.getAttributes()).fold(new MapRecord(), asField());
            }
        };
    }

    private Callable2<? super Record, ? super Attribute, Record> asField() {
        return new Callable2<Record, Attribute, Record>() {
            public Record call(Record mapRecord, Attribute attribute) throws Exception {
                return mapRecord.set(Keywords.keyword(attribute.getName()), attribute.getValue());
            }
        };
    }

    public Number add(Keyword recordName, Sequence<Record> records) {
        sdb.batchPutAttributes(new BatchPutAttributesRequest(recordName.name(), records.map(toItem()).toList()));
        return records.size();
    }

    public static Callable1<? super Record, ReplaceableItem> toItem() {
        return new Callable1<Record, ReplaceableItem>() {
            public ReplaceableItem call(Record record) throws Exception {
                return new ReplaceableItem(UUID.randomUUID().toString(), record.fields().map(asAttribute()).toList());
            }
        };
    }

    public static Callable1<? super Pair<Keyword, Object>, ReplaceableAttribute> asAttribute() {
        return new Callable1<Pair<Keyword, Object>, ReplaceableAttribute>() {
            public ReplaceableAttribute call(Pair<Keyword, Object> pair) throws Exception {
                return new ReplaceableAttribute(pair.first().name(), pair.second().toString(), true);
            }
        };
    }

    public Number remove(Keyword recordName, Predicate<? super Record> predicate) {
        return 0;
    }
}

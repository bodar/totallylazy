package com.googlecode.totallylazy.records.simpledb;

import com.amazonaws.services.simpledb.AmazonSimpleDB;
import com.amazonaws.services.simpledb.model.BatchDeleteAttributesRequest;
import com.amazonaws.services.simpledb.model.BatchPutAttributesRequest;
import com.amazonaws.services.simpledb.model.CreateDomainRequest;
import com.amazonaws.services.simpledb.model.DeletableItem;
import com.amazonaws.services.simpledb.model.DeleteDomainRequest;
import com.amazonaws.services.simpledb.model.Item;
import com.amazonaws.services.simpledb.model.SelectRequest;
import com.googlecode.totallylazy.Callable1;
import com.googlecode.totallylazy.Predicate;
import com.googlecode.totallylazy.Sequence;
import com.googlecode.totallylazy.records.AbstractRecords;
import com.googlecode.totallylazy.records.Keyword;
import com.googlecode.totallylazy.records.Record;
import com.googlecode.totallylazy.records.SourceRecord;
import com.googlecode.totallylazy.records.simpledb.mappings.Mappings;

import java.util.List;

import static com.googlecode.totallylazy.Sequences.sequence;

public class SimpleDBRecords extends AbstractRecords {
    private final AmazonSimpleDB sdb;
    private final Mappings mappings;

    public SimpleDBRecords(final AmazonSimpleDB sdb, final Mappings mappings) {
        this.mappings = mappings;
        this.sdb = sdb;
    }

    public SimpleDBRecords(final AmazonSimpleDB sdb) {
        this(sdb, new Mappings());
    }

    @Override
    public void define(Keyword recordName, Keyword<?>... fields) {
        super.define(recordName, fields);
        sdb.createDomain(new CreateDomainRequest(recordName.name()));
    }

    @Override
    public boolean exists(Keyword recordName) {
        return sdb.listDomains().withDomainNames(recordName.name()).getDomainNames().size() > 0;
    }

    public Sequence<Record> get(Keyword recordName) {
        Sequence<Item> result = sequence(sdb.select(new SelectRequest("select * from `" + recordName.name() + "`", true)).getItems());
        return result.map(mappings.asRecord(definitions(recordName)));
    }

    public Number add(Keyword recordName, Sequence<Record> records) {
        if (records.isEmpty()) {
            return 0;
        }
        sdb.batchPutAttributes(new BatchPutAttributesRequest(recordName.name(), records.map(mappings.toReplaceableItem()).toList()));
        return records.size();
    }

    public Number remove(Keyword recordName, Predicate<? super Record> predicate) {
        if(!exists(recordName)){
            return 0;
        }
        Sequence<Record> items = get(recordName).filter(predicate).realise();
        if (items.isEmpty()) {
            return 0;
        }
        sdb.batchDeleteAttributes(new BatchDeleteAttributesRequest(recordName.name(), items.map(asItem()).toList()));
        return items.size();
    }

    @Override
    public List<Keyword<?>> undefine(Keyword recordName){
        sdb.deleteDomain(new DeleteDomainRequest(recordName.name()));
        return super.undefine(recordName);
    }
    
    private Callable1<? super Record, DeletableItem> asItem() {
        return new Callable1<Record, DeletableItem>() {
            public DeletableItem call(Record record) throws Exception {
                Item item = ((SourceRecord<Item>) record).value();
                return new DeletableItem().withName(item.getName());
            }
        };
    }
}

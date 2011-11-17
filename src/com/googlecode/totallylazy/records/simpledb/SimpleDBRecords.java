package com.googlecode.totallylazy.records.simpledb;

import com.amazonaws.services.simpledb.AmazonSimpleDB;
import com.amazonaws.services.simpledb.model.BatchDeleteAttributesRequest;
import com.amazonaws.services.simpledb.model.BatchPutAttributesRequest;
import com.amazonaws.services.simpledb.model.CreateDomainRequest;
import com.amazonaws.services.simpledb.model.DeletableItem;
import com.amazonaws.services.simpledb.model.DeleteDomainRequest;
import com.amazonaws.services.simpledb.model.Item;
import com.googlecode.totallylazy.Callable1;
import com.googlecode.totallylazy.Predicate;
import com.googlecode.totallylazy.Sequence;
import com.googlecode.totallylazy.Sequences;
import com.googlecode.totallylazy.numbers.Numbers;
import com.googlecode.totallylazy.records.AbstractRecords;
import com.googlecode.totallylazy.records.Keyword;
import com.googlecode.totallylazy.records.Keywords;
import com.googlecode.totallylazy.records.Record;
import com.googlecode.totallylazy.records.SourceRecord;
import com.googlecode.totallylazy.records.simpledb.mappings.Mappings;

import java.io.PrintStream;
import java.util.List;

import static com.googlecode.totallylazy.Streams.nullPrintStream;
import static com.googlecode.totallylazy.numbers.Numbers.sum;
import static com.googlecode.totallylazy.records.SelectCallable.select;
import static com.googlecode.totallylazy.records.sql.expressions.SelectBuilder.from;

public class SimpleDBRecords extends AbstractRecords {
    private final AmazonSimpleDB sdb;
    private final Mappings mappings;
    private final PrintStream logger;
    private final boolean consistentRead;

    public SimpleDBRecords(final AmazonSimpleDB sdb, boolean consistentRead, final Mappings mappings, final PrintStream logger) {
        this.consistentRead = consistentRead;
        this.mappings = mappings;
        this.sdb = sdb;
        this.logger = logger;
    }

    public SimpleDBRecords(final AmazonSimpleDB sdb, boolean consistentRead) {
        this(sdb, consistentRead, new Mappings(), nullPrintStream());
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
        return new SimpleDBSequence<Record>(sdb, from(recordName).select(definitions(recordName)), mappings, mappings.asRecord(definitions(recordName)), logger, consistentRead);
    }

    public Number add(final Keyword recordName, Sequence<Record> records) {
        if (records.isEmpty()) {
            return 0;
        }

        return records.recursive(Sequences.<Record>splitAt(25)).
                mapConcurrently(putAttributes(recordName)).
                reduce(sum());
    }

    public Number remove(Keyword recordName, Predicate<? super Record> predicate) {
        if (!exists(recordName)) {
            return 0;
        }
        Sequence<Record> items = get(recordName).filter(predicate).realise();
        if (items.isEmpty()) {
            return 0;
        }

        return items.recursive(Sequences.<Record>splitAt(25)).
                mapConcurrently(deleteAttributes(recordName)).
                reduce(sum());
    }

    @Override
    public Number remove(Keyword recordName) {
        Record head = get(recordName).map(select(Keywords.keyword("count(*)", String.class))).head();
        Number result = Numbers.valueOf(head.get(Keywords.keyword("Count", String.class)));
        List<Keyword<?>> undefine = undefine(recordName);
        define(recordName, undefine.toArray(new Keyword[0]));
        return result;
    }

    @Override
    public List<Keyword<?>> undefine(Keyword recordName) {
        sdb.deleteDomain(new DeleteDomainRequest(recordName.name()));
        return super.undefine(recordName);
    }

    @SuppressWarnings("unchecked")
    private Callable1<? super Record, DeletableItem> asItem() {
        return new Callable1<Record, DeletableItem>() {
            public DeletableItem call(Record record) throws Exception {
                Item item = ((SourceRecord<Item>) record).value();
                return new DeletableItem().withName(item.getName());
            }
        };
    }

    private Callable1<Sequence<Record>, Number> putAttributes(final Keyword recordName) {
        return new Callable1<Sequence<Record>, Number>() {
            public Number call(Sequence<Record> batch) throws Exception {
                sdb.batchPutAttributes(new BatchPutAttributesRequest(recordName.name(), batch.map(mappings.toReplaceableItem()).toList()));
                return batch.size();
            }
        };
    }

    private Callable1<Sequence<Record>, Number> deleteAttributes(final Keyword recordName) {
        return new Callable1<Sequence<Record>, Number>() {
            public Number call(Sequence<Record> batch) throws Exception {
                sdb.batchDeleteAttributes(new BatchDeleteAttributesRequest(recordName.name(), batch.map(asItem()).toList()));
                return batch.size();
            }
        };
    }


}

package com.googlecode.totallylazy.records.lucene;

import com.googlecode.totallylazy.LazyException;
import com.googlecode.totallylazy.Predicate;
import com.googlecode.totallylazy.Sequence;
import com.googlecode.totallylazy.records.AbstractRecords;
import com.googlecode.totallylazy.records.Keyword;
import com.googlecode.totallylazy.records.Record;
import com.googlecode.totallylazy.records.lucene.mappings.Mappings;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;

import java.io.IOException;

import static com.googlecode.totallylazy.numbers.Numbers.increment;
import static com.googlecode.totallylazy.records.SelectCallable.select;
import static com.googlecode.totallylazy.records.lucene.Lucene.and;
import static com.googlecode.totallylazy.records.lucene.Lucene.query;
import static com.googlecode.totallylazy.records.lucene.Lucene.record;

public class LuceneRecords extends AbstractRecords {
    private final Directory directory;
    private final IndexWriter writer;
    private final Mappings mappings;

    public LuceneRecords(final Directory directory, final IndexWriter writer, final Mappings mappings) throws IOException {
        this.directory = directory;
        this.writer = writer;
        this.mappings = mappings;
    }

    public Sequence<Record> get(final Keyword recordName) {
        return new RecordSequence(directory, mappings, definitions(recordName), record(recordName));
    }

    public boolean exists(Keyword recordName) {
        throw new UnsupportedOperationException();
    }

    public Number add(Keyword recordName, Sequence<Keyword> fields, Sequence<Record> records) {
        try {
            Number count = 0;
            for (Document document : records.map(select(fields)).map(mappings.asDocument(recordName, definitions(recordName)))) {
                writer.addDocument(document);
                count = increment(count);
            }
            writer.commit();
            return count;
        } catch (IOException e) {
            throw new LazyException(e);
        }
    }

    public Number set(Keyword recordName, Predicate<? super Record> predicate, Sequence<Keyword> fields, Record record) {
        throw new UnsupportedOperationException();
    }

    public Number remove(Keyword recordName, Predicate<? super Record> predicate) {
        return remove(and(record(recordName), query(predicate)));
    }

    public Number remove(Keyword recordName) {
        return remove(record(recordName));
    }

    private Number remove(Query query) {
        try {
            writer.deleteDocuments(query);
            return -1;
        } catch (IOException e) {
            throw new LazyException(e);
        }
    }

}

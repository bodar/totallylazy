package com.googlecode.totallylazy.records.lucene;

import com.googlecode.totallylazy.Predicate;
import com.googlecode.totallylazy.Sequence;
import com.googlecode.totallylazy.records.Keyword;
import com.googlecode.totallylazy.records.Record;
import com.googlecode.totallylazy.records.lucene.mappings.Mappings;
import org.apache.lucene.search.Query;
import org.apache.lucene.store.Directory;

import java.util.Iterator;

import static com.googlecode.totallylazy.records.lucene.Lucene.and;
import static com.googlecode.totallylazy.records.lucene.Lucene.query;

public class RecordSequence extends Sequence<Record> {
    private final Directory directory;
    private final Mappings mappings;
    private final Sequence<Keyword> definitions;
    private final Query query;

    public RecordSequence(Directory directory, Mappings mappings, Sequence<Keyword> definitions, Query query) {
        this.directory = directory;
        this.mappings = mappings;
        this.definitions = definitions;
        this.query = query;
    }

    public Iterator<Record> iterator() {
        return new DocumentIterator(directory, mappings, definitions, query);
    }

    @Override
    public Sequence<Record> filter(Predicate<? super Record> predicate) {
        return new RecordSequence(directory, mappings, definitions, and(query, query(predicate)));
    }
}

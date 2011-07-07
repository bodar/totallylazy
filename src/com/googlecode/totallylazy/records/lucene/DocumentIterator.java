package com.googlecode.totallylazy.records.lucene;

import com.googlecode.totallylazy.LazyException;
import com.googlecode.totallylazy.iterators.ReadOnlyIterator;
import com.googlecode.totallylazy.records.Keyword;
import com.googlecode.totallylazy.records.MapRecord;
import com.googlecode.totallylazy.records.Record;
import com.googlecode.totallylazy.records.lucene.mappings.Mappings;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.store.Directory;

import java.io.IOException;

import static com.googlecode.totallylazy.Callables.first;
import static com.googlecode.totallylazy.Predicates.*;
import static com.googlecode.totallylazy.Sequences.sequence;
import static com.googlecode.totallylazy.records.memory.MemoryRecords.updateValues;

public class DocumentIterator extends ReadOnlyIterator<Record>{
    private int index = 0;
    private final IndexReader reader;
    private final Mappings mappings;

    public DocumentIterator(final Directory directory, final Mappings mappings) {
        this.mappings = mappings;
        try {
            this.reader = IndexReader.open(directory);
        } catch (IOException e) {
            throw new LazyException(e);
        }
    }

    public boolean hasNext() {
        return index < reader.maxDoc();
    }

    public Record next() {
        try {
            Document document = reader.document(index);
            index++;
            return sequence(document.getFields()).
                    map(mappings.asPair()).
                    filter(where(first(Keyword.class), is(not(Lucene.RECORD_KEY)))).
                    fold(new MapRecord(), updateValues());
        } catch (IOException e) {
            throw new LazyException(e);
        }
    }
}

package com.googlecode.totallylazy.records.lucene;

import com.googlecode.totallylazy.Callable1;
import com.googlecode.totallylazy.CloseableList;
import com.googlecode.totallylazy.Predicate;
import com.googlecode.totallylazy.Sequence;
import com.googlecode.totallylazy.records.Record;
import org.apache.lucene.document.Document;
import org.apache.lucene.search.Query;

import java.io.PrintStream;
import java.util.Iterator;

import static com.googlecode.totallylazy.records.lucene.Lucene.and;

public class RecordSequence extends Sequence<Record> {
    private final LuceneStorage storage;
    private final Query query;
    private final PrintStream printStream;
    private final Lucene lucene;
    private final Callable1<? super Document,Record> documentToRecord;
    private CloseableList closeables;

    public RecordSequence(final Lucene lucene, final LuceneStorage storage, final Query query,
                          final Callable1<? super Document, Record> documentToRecord, final PrintStream printStream, CloseableList closeables) {
        this.lucene = lucene;
        this.storage = storage;
        this.query = query;
        this.documentToRecord = documentToRecord;
        this.printStream = printStream;
        this.closeables = closeables;
    }


    public Iterator<Record> iterator() {
        return closeables.manage(new LuceneIterator(storage, query, documentToRecord, printStream));
    }

    @Override
    public Sequence<Record> filter(Predicate<? super Record> predicate) {
        return new RecordSequence(lucene, storage, and(query, lucene.query(predicate)), documentToRecord, printStream, closeables);
    }
}

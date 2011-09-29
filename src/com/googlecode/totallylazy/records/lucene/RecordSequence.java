package com.googlecode.totallylazy.records.lucene;

import com.googlecode.totallylazy.Callable1;
import com.googlecode.totallylazy.Predicate;
import com.googlecode.totallylazy.Sequence;
import com.googlecode.totallylazy.records.Record;
import org.apache.lucene.document.Document;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.store.Directory;

import java.io.PrintStream;
import java.util.Iterator;

import static com.googlecode.totallylazy.records.lucene.Lucene.and;

public class RecordSequence extends Sequence<Record> {
    private final Directory directory;
    private final Query query;
    private final PrintStream printStream;
    private final Lucene lucene;
    private final Callable1<? super Document,Record> documentToRecord;

    public RecordSequence(final Lucene lucene, final Directory directory, final Query query,
                          final Callable1<? super Document, Record> documentToRecord, final PrintStream printStream) {
        this.lucene = lucene;
        this.directory = directory;
        this.query = query;
        this.documentToRecord = documentToRecord;
        this.printStream = printStream;
    }

    public Iterator<Record> iterator() {
        return new LuceneIterator(directory, query, documentToRecord, printStream);
    }

    @Override
    public Sequence<Record> filter(Predicate<? super Record> predicate) {
        return new RecordSequence(lucene, directory, and(query, lucene.query(predicate)), documentToRecord, printStream);
    }
}

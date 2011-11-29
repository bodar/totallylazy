package com.googlecode.totallylazy.records.lucene;

import com.googlecode.totallylazy.Callable1;
import com.googlecode.totallylazy.CloseableList;
import com.googlecode.totallylazy.LazyException;
import com.googlecode.totallylazy.Predicate;
import com.googlecode.totallylazy.Sequence;
import com.googlecode.totallylazy.records.AbstractRecords;
import com.googlecode.totallylazy.records.Keyword;
import com.googlecode.totallylazy.records.Queryable;
import com.googlecode.totallylazy.records.Record;
import com.googlecode.totallylazy.records.lucene.mappings.Mappings;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.store.Directory;

import java.io.Closeable;
import java.io.IOException;
import java.io.PrintStream;

import static com.googlecode.totallylazy.Closeables.using;
import static com.googlecode.totallylazy.Streams.nullOutputStream;
import static com.googlecode.totallylazy.numbers.Numbers.increment;
import static com.googlecode.totallylazy.records.lucene.Lucene.and;
import static com.googlecode.totallylazy.records.lucene.Lucene.record;

public class LuceneRecords extends AbstractRecords implements Queryable<Query> {
    private final LuceneStorage storage;
    private final Mappings mappings;
    private final PrintStream printStream;
    private final Lucene lucene;

    public LuceneRecords(final LuceneStorage storage, final Mappings mappings, final PrintStream printStream) throws IOException {
        this.storage = storage;
        this.mappings = mappings;
        this.printStream = printStream;
        lucene = new Lucene(this.mappings);
    }

    public LuceneRecords(final LuceneStorage storage) throws IOException {
        this(storage, new Mappings(), new PrintStream(nullOutputStream()));
    }

    public Sequence<Record> query(final Query query, final Sequence<Keyword> definitions) {
        return new RecordSequence(lucene, storage, query, mappings.asRecord(definitions), printStream);
    }

    public Sequence<Record> get(final Keyword recordName) {
        return query(record(recordName), definitions(recordName));
    }

    public Number add(Keyword recordName, Sequence<Record> records) {
        try {
            Number count = 0;
            for (Document document : records.map(mappings.asDocument(recordName, definitions(recordName)))) {
                storage.writer().addDocument(document);
                count = increment(count);
            }
            storage.writer().commit();
            return count;
        } catch (IOException e) {
            throw new LazyException(e);
        }
    }

    public Number remove(Keyword recordName, Predicate<? super Record> predicate) {
        return remove(and(record(recordName), lucene.query(predicate)));
    }

    public Number remove(Keyword recordName) {
        return remove(record(recordName));
    }

    public Number remove(Query query) {
        try {
            int result = count(query);
            storage.writer().deleteDocuments(query);
            storage.writer().commit();
            return result;
        } catch (IOException e) {
            throw new LazyException(e);
        }
    }

    public int count(final Query query) {
        try {
            return storage.searcher().search(query, Integer.MAX_VALUE).totalHits;
        } catch (IOException e) {
            return 0;
        }
    }
}

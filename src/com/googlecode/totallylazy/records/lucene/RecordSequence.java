package com.googlecode.totallylazy.records.lucene;

import com.googlecode.totallylazy.Callable1;
import com.googlecode.totallylazy.Callables;
import com.googlecode.totallylazy.Iterators;
import com.googlecode.totallylazy.LazyException;
import com.googlecode.totallylazy.Predicate;
import com.googlecode.totallylazy.Sequence;
import com.googlecode.totallylazy.iterators.ArrayIterator;
import com.googlecode.totallylazy.records.Keyword;
import com.googlecode.totallylazy.records.MapRecord;
import com.googlecode.totallylazy.records.Record;
import com.googlecode.totallylazy.records.lucene.mappings.Mappings;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Fieldable;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.store.Directory;
import org.hsqldb.index.Index;

import java.io.IOException;
import java.io.PrintStream;
import java.util.Iterator;
import java.util.List;

import static com.googlecode.totallylazy.Predicates.is;
import static com.googlecode.totallylazy.Predicates.not;
import static com.googlecode.totallylazy.Predicates.where;
import static com.googlecode.totallylazy.Sequences.sequence;
import static com.googlecode.totallylazy.records.lucene.Lucene.and;
import static com.googlecode.totallylazy.records.memory.MemoryRecords.updateValues;

public class RecordSequence extends Sequence<Record> {
    private final Directory directory;
    private final Mappings mappings;
    private final Sequence<Keyword> definitions;
    private final Query query;
    private final PrintStream printStream;
    private final Lucene lucene;

    public RecordSequence(Directory directory, Mappings mappings, Sequence<Keyword> definitions, Query query, PrintStream printStream) {
        this.directory = directory;
        this.mappings = mappings;
        this.definitions = definitions;
        this.query = query;
        this.printStream = printStream;
        lucene = new Lucene(this.mappings);
    }

    public Iterator<Record> iterator() {
        try {
            final IndexSearcher searcher = new IndexSearcher(directory);
            Iterator<Document> documentIterator = Iterators.map(new ArrayIterator<ScoreDoc>(scoreDocs(searcher)), asDocument(searcher));
            return Iterators.map(documentIterator, asRecord(mappings, definitions));
        } catch (IOException e) {
            throw new UnsupportedOperationException(e);
        }
    }

    private ScoreDoc[] scoreDocs(final IndexSearcher searcher) throws IOException {
        printStream.println("LUCENE = " + query);
        return searcher.search(query, Integer.MAX_VALUE).scoreDocs;
    }

    public static Callable1<ScoreDoc, Document> asDocument(final IndexSearcher searcher) {
        return new Callable1<ScoreDoc, Document>() {
            public Document call(ScoreDoc scoreDoc) throws Exception {
                return searcher.doc(scoreDoc.doc);
            }
        };
    }

    public static Callable1<? super Document, Record> asRecord(final Mappings mappings, final Sequence<Keyword> definitions) {
        return new Callable1<Document, Record>() {
            public Record call(Document document) throws Exception {
                return sequence(document.getFields()).
                        map(mappings.asPair(definitions)).
                        filter(where(Callables.first(Keyword.class), is(not(Lucene.RECORD_KEY)))).
                        fold(new MapRecord(), updateValues());
            }
        };
    }

    @Override
    public Sequence<Record> filter(Predicate<? super Record> predicate) {
        return new RecordSequence(directory, mappings, definitions, and(query, lucene.query(predicate)), printStream);
    }
}

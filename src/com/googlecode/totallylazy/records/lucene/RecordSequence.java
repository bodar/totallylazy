package com.googlecode.totallylazy.records.lucene;

import com.googlecode.totallylazy.Callable1;
import com.googlecode.totallylazy.Iterators;
import com.googlecode.totallylazy.Predicate;
import com.googlecode.totallylazy.Sequence;
import com.googlecode.totallylazy.iterators.ArrayIterator;
import com.googlecode.totallylazy.records.Keyword;
import com.googlecode.totallylazy.records.Record;
import com.googlecode.totallylazy.records.lucene.mappings.Mappings;
import org.apache.lucene.document.Document;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.store.Directory;

import java.io.IOException;
import java.io.PrintStream;
import java.util.Iterator;

import static com.googlecode.totallylazy.records.lucene.Lucene.and;

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
            return Iterators.map(documentIterator, mappings.asRecord(definitions));
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

    @Override
    public Sequence<Record> filter(Predicate<? super Record> predicate) {
        return new RecordSequence(directory, mappings, definitions, and(query, lucene.query(predicate)), printStream);
    }
}

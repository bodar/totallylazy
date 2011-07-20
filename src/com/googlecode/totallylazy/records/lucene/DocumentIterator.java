package com.googlecode.totallylazy.records.lucene;

import com.googlecode.totallylazy.LazyException;
import com.googlecode.totallylazy.Sequence;
import com.googlecode.totallylazy.iterators.ReadOnlyIterator;
import com.googlecode.totallylazy.records.Keyword;
import com.googlecode.totallylazy.records.MapRecord;
import com.googlecode.totallylazy.records.Record;
import com.googlecode.totallylazy.records.lucene.mappings.Mappings;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Fieldable;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;

import java.io.Closeable;
import java.io.IOException;
import java.io.PrintStream;
import java.util.List;

import static com.googlecode.totallylazy.Callables.first;
import static com.googlecode.totallylazy.Predicates.*;
import static com.googlecode.totallylazy.Sequences.sequence;
import static com.googlecode.totallylazy.records.memory.MemoryRecords.updateValues;

public class DocumentIterator extends ReadOnlyIterator<Document> implements Closeable {
    private int index = 0;
    private final Directory directory;
    private final Query query;
    private final PrintStream printStream;
    private ScoreDoc[] scoreDocs;
    private IndexSearcher searcher;

    public DocumentIterator(final Directory directory, final Query query, final PrintStream printStream) {
        this.directory = directory;
        this.query = query;
        this.printStream = printStream;
    }

    public boolean hasNext() {
        return index < scoreDocs().length;
    }

    public Document next() {
        try {
            return searcher().doc(scoreDocs()[index++].doc);
        } catch (IOException e) {
            throw new LazyException(e);
        }
    }

    private ScoreDoc[] scoreDocs() {
        try {
            if (scoreDocs == null) {
                printStream.println("LUCENE = " + query);
                scoreDocs = searcher().search(query, Integer.MAX_VALUE).scoreDocs;
            }
            return scoreDocs;
        } catch (IOException e) {
            throw new LazyException(e);
        }
    }

    private IndexSearcher searcher() {
        if (searcher == null) {
            try {
                searcher = new IndexSearcher(directory);
            } catch (IOException e) {
                throw new LazyException(e);
            }
        }
        return searcher;
    }

    public void close() throws IOException {
        if (searcher != null) {
            searcher.close();
        }
    }
}

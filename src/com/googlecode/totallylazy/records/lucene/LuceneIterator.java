package com.googlecode.totallylazy.records.lucene;

import com.googlecode.totallylazy.Callable1;
import com.googlecode.totallylazy.CloseableList;
import com.googlecode.totallylazy.Closeables;
import com.googlecode.totallylazy.iterators.StatefulIterator;
import com.googlecode.totallylazy.records.Record;
import org.apache.lucene.document.Document;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.store.Directory;

import java.io.Closeable;
import java.io.IOException;
import java.io.PrintStream;

import static com.googlecode.totallylazy.Arrays.containsIndex;

public class LuceneIterator extends StatefulIterator<Record> implements Closeable{
    private final LuceneStorage storage;
    private final Query query;
    private final Callable1<? super Document, Record> documentToRecord;
    private final PrintStream printStream;
    private ScoreDoc[] scoreDocs;
    private int index = 0;
    private Searcher searcher;

    public LuceneIterator(LuceneStorage storage, Query query, Callable1<? super Document, Record> documentToRecord, PrintStream printStream) {
        this.storage = storage;
        this.query = query;
        this.documentToRecord = documentToRecord;
        this.printStream = printStream;
    }

    @Override
    protected Record getNext() throws Exception {
        if(!containsIndex(scoreDocs(), index)){
            close();
            return finished();
        }
        Document document = searcher().document(scoreDocs()[index++].doc);
        return documentToRecord.call(document);
    }

    private ScoreDoc[] scoreDocs() throws IOException {
        if( scoreDocs == null) {
            printStream.println("LUCENE = " + query);
            scoreDocs = searcher().search(query).scoreDocs;
        }
        return scoreDocs;
    }

    private Searcher searcher() throws IOException {
        if( searcher == null){
            searcher = storage.searcher();
        }
        return searcher;
    }

    @Override
    public void close() throws IOException {
        Closeables.close(searcher);
    }
}

package com.googlecode.totallylazy.records.lucene;

import com.googlecode.totallylazy.Callable1;
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

public class LuceneIterator extends StatefulIterator<Record>{
    private final LuceneStorage storage;
    private final Query query;
    private final Callable1<? super Document, Record> documentToRecord;
    private final PrintStream printStream;
    private ScoreDoc[] scoreDocs;
    private int index = 0;

    public LuceneIterator(LuceneStorage storage, Query query, Callable1<? super Document, Record> documentToRecord, PrintStream printStream) {
        this.storage = storage;
        this.query = query;
        this.documentToRecord = documentToRecord;
        this.printStream = printStream;
    }

    @Override
    protected Record getNext() throws Exception {
        if(!containsIndex(scoreDocs(), index)){
            return finished();
        }
        Document document = storage.searcher().doc(scoreDocs()[index++].doc);
        return documentToRecord.call(document);
    }

    private ScoreDoc[] scoreDocs() throws IOException {
        if( scoreDocs == null) {
            printStream.println("LUCENE = " + query);
            scoreDocs = storage.searcher().search(query, Integer.MAX_VALUE).scoreDocs;
        }
        return scoreDocs;
    }
}

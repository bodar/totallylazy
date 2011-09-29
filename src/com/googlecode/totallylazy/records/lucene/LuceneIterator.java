package com.googlecode.totallylazy.records.lucene;

import com.googlecode.totallylazy.Arrays;
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

public class LuceneIterator extends StatefulIterator<Record> implements Closeable{
    private final Directory directory;
    private final Query query;
    private final Callable1<? super Document, Record> documentToRecord;
    private final PrintStream printStream;
    private IndexSearcher indexSearcher;
    private ScoreDoc[] scoreDocs;
    private int index = 0;

    public LuceneIterator(Directory directory, Query query, Callable1<? super Document, Record> documentToRecord, PrintStream printStream) {
        this.directory = directory;
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
        Document document = indexSearcher().doc(scoreDocs()[index++].doc);
        return documentToRecord.call(document);
    }

    private IndexSearcher indexSearcher() throws IOException {
        if(indexSearcher == null){
            indexSearcher = new IndexSearcher(directory);
        }
        return indexSearcher;
    }

    private ScoreDoc[] scoreDocs() throws IOException {
        if( scoreDocs == null) {
            printStream.println("LUCENE = " + query);
            scoreDocs = indexSearcher().search(query, Integer.MAX_VALUE).scoreDocs;
        }
        return scoreDocs;
    }

    public void close() throws IOException {
        Closeables.close(indexSearcher);
    }
}

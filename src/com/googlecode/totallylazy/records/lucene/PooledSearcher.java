package com.googlecode.totallylazy.records.lucene;

import com.googlecode.totallylazy.Callable1;
import org.apache.lucene.document.Document;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TopDocs;

import java.io.IOException;

public class PooledSearcher implements Searcher {
    private final Callable1<Searcher, Void> checkin;
    final IndexSearcher searcher;

    public PooledSearcher(IndexSearcher searcher, Callable1<Searcher, Void> checkin) {
        this.searcher = searcher;
        this.checkin = checkin;
    }

    @Override
    public TopDocs search(Query query) throws IOException {
        return searcher.search(query, Integer.MAX_VALUE);
    }

    @Override
    public Document document(int id) throws IOException {
        return searcher.doc(id);
    }

    @Override
    public void close() throws IOException {
        try {
            checkin.call(this);
        } catch (Exception e) {
            throw new IOException(e);
        }
    }
}

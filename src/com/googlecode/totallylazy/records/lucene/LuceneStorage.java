package com.googlecode.totallylazy.records.lucene;

import com.googlecode.totallylazy.Callable1;
import com.googlecode.totallylazy.Sequence;
import org.apache.lucene.document.Document;
import org.apache.lucene.search.Query;

import java.io.Closeable;
import java.io.IOException;

public interface LuceneStorage extends Closeable{
    Number add(Sequence<Document> documents) throws IOException;

    Number delete(Query query) throws IOException;

    void deleteAll() throws IOException;

    int count(Query query) throws IOException;

    <T> T search(Callable1<Searcher, T> callable) throws IOException;

    Searcher searcher() throws IOException;
}

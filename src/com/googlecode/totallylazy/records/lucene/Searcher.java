package com.googlecode.totallylazy.records.lucene;

import org.apache.lucene.document.Document;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TopDocs;

import java.io.Closeable;
import java.io.IOException;

public interface Searcher extends Closeable{
    TopDocs search(Query query) throws IOException;

    Document document(int id) throws IOException;
}

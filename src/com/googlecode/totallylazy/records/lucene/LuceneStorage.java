package com.googlecode.totallylazy.records.lucene;

import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.search.IndexSearcher;

import java.io.Closeable;
import java.io.IOException;

public interface LuceneStorage extends Closeable{
    IndexWriter writer() throws IOException;

    IndexSearcher searcher() throws IOException;
}

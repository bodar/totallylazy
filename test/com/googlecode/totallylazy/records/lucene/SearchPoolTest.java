package com.googlecode.totallylazy.records.lucene;

import org.apache.lucene.analysis.KeywordAnalyzer;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.Version;
import org.junit.Test;

import java.io.IOException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.sameInstance;

public class SearchPoolTest {
    @Test
    public void returnsTheSameSearchIfNotWrites() throws Exception {
        SearcherPool pool = new OptimisedPool(emptyDirectory());
        Searcher searcher1 = pool.searcher();
        assertThat(searcher1, is(notNullValue()));
        assertThat(pool.size(), is(1));

        Searcher searcher2 = pool.searcher();
        assertThat(searcher2, is(notNullValue()));
        assertThat(pool.size(), is(1));

        assertThat(searcher1, is(sameInstance(searcher2)));
    }

    private RAMDirectory emptyDirectory() throws IOException {
        RAMDirectory ramDirectory = new RAMDirectory();
        IndexWriter writer = new IndexWriter(ramDirectory, new IndexWriterConfig(Version.LUCENE_30, new KeywordAnalyzer()));
        writer.commit();
        return ramDirectory;
    }

    @Test
    public void closeSearcherWhenDirty() throws Exception {
        SearcherPool pool = new OptimisedPool(emptyDirectory());
        Searcher searcher1 = pool.searcher();
        assertThat(pool.size(), is(1));
        searcher1.close();
        assertThat(pool.size(), is(1));
        pool.markAsDirty();
        assertThat(pool.size(), is(0));
    }

    @Test
    public void closeDirtyCheckedOutSearcherWhenTheyAreReturned() throws Exception {
        SearcherPool pool = new OptimisedPool(emptyDirectory());
        Searcher searcher1 = pool.searcher();
        Searcher searcher2 = pool.searcher();
        assertThat(pool.size(), is(1));
        searcher1.close();
        assertThat(pool.size(), is(1));

        pool.markAsDirty();

        assertThat(pool.size(), is(1));
        searcher2.close();
        assertThat(pool.size(), is(0));
    }
}

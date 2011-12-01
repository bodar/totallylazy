package com.googlecode.totallylazy.records.lucene;

import org.apache.lucene.search.MatchAllDocsQuery;
import org.apache.lucene.store.RAMDirectory;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class LuceneStorageTest {
    @Test
    public void canSearchAnEmptyIndex() throws Exception{
        LuceneStorage storage = new OptimisedStorage(new RAMDirectory());
        assertThat(storage.count(new MatchAllDocsQuery()), is(0));
    }
}

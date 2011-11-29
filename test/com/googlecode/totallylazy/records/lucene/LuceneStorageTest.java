package com.googlecode.totallylazy.records.lucene;

import org.apache.lucene.store.RAMDirectory;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

public class LuceneStorageTest {
    @Test
    public void canGetSearcherBeforeWriter() throws Exception{
        LuceneStorage storage = new OptimisedStorage(new RAMDirectory());
        assertThat(storage.searcher(), is(notNullValue()));
        assertThat(storage.writer(), is(notNullValue()));
    }
}

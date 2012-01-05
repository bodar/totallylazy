package com.googlecode.totallylazy.records.lucene;

import org.apache.lucene.document.Document;
import org.apache.lucene.search.MatchAllDocsQuery;
import org.apache.lucene.store.RAMDirectory;
import org.junit.Test;

import java.io.IOException;

import static com.googlecode.totallylazy.Sequences.sequence;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class LuceneStorageTest {
    @Test
    public void canSearchAnEmptyIndex() throws Exception {
        LuceneStorage storage = new OptimisedStorage(new RAMDirectory());
        assertThat(storage.count(new MatchAllDocsQuery()), is(0));
    }

    @Test
    public void canDeleteAll() throws IOException {
        LuceneStorage storage = new OptimisedStorage(new RAMDirectory());
        storage.add(sequence(new Document()));
        storage.add(sequence(new Document()));
        assertThat(storage.count(new MatchAllDocsQuery()), is(2));
        storage.deleteAll();
        assertThat(storage.count(new MatchAllDocsQuery()), is(0));
    }
}

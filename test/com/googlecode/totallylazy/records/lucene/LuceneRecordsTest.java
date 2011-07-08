package com.googlecode.totallylazy.records.lucene;

import com.googlecode.totallylazy.records.AbstractRecordsTests;
import com.googlecode.totallylazy.records.Records;
import com.googlecode.totallylazy.records.lucene.mappings.Mappings;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.NIOFSDirectory;
import org.apache.lucene.util.Version;
import org.junit.Ignore;

import static com.googlecode.totallylazy.Files.temporaryDirectory;

public class LuceneRecordsTest extends AbstractRecordsTests {
    @Override
    protected Records createRecords() throws Exception {
        Version version = Version.LUCENE_33;
        final NIOFSDirectory directory = new NIOFSDirectory(temporaryDirectory());
        IndexWriter writer = new IndexWriter(directory, new IndexWriterConfig(version, new StandardAnalyzer(version)));
        return new LuceneRecords(directory, writer, new Mappings());
    }

    @Override @Ignore("NOT currently not supported")
    public void supportsIsNullAndNotNull() throws Exception {
    }

    @Override @Ignore("NOT currently not supported")
    public void supportsFilteringWithNot() throws Exception {
    }
}

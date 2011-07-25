package com.googlecode.totallylazy.records.lucene;

import com.googlecode.totallylazy.Sequence;
import com.googlecode.totallylazy.matchers.NumberMatcher;
import com.googlecode.totallylazy.records.AbstractRecordsTests;
import com.googlecode.totallylazy.records.Record;
import com.googlecode.totallylazy.records.Records;
import com.googlecode.totallylazy.records.lucene.mappings.Mappings;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.NIOFSDirectory;
import org.apache.lucene.util.Version;
import org.junit.Test;

import static com.googlecode.totallylazy.Files.temporaryDirectory;
import static org.hamcrest.MatcherAssert.assertThat;

public class LuceneRecordsTest extends AbstractRecordsTests {

    public static final Version VERSION = Version.LUCENE_33;
    public static final StandardAnalyzer ANALYZER = new StandardAnalyzer(VERSION);

    @Override
    protected Records createRecords() throws Exception {
        final Directory directory = new NIOFSDirectory(temporaryDirectory());
        IndexWriter writer = new IndexWriter(directory, new IndexWriterConfig(VERSION, ANALYZER));
        return new LuceneRecords(directory, writer, new Mappings(), System.out);
    }

    @Test
    public void canQueryIndexDirectly() throws Exception {
        QueryParser parser = new QueryParser(VERSION, null, ANALYZER);
        Sequence<Record> results = ((LuceneRecords) records).query(parser.parse("+firstName:dan"));
        assertThat(results.size(), NumberMatcher.is(1));
    }
}

package com.googlecode.totallylazy.records.lucene;

import com.googlecode.totallylazy.Callable1;
import com.googlecode.totallylazy.Closeables;
import com.googlecode.totallylazy.Sequence;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.KeywordAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.search.Query;
import org.apache.lucene.store.Directory;
import org.apache.lucene.util.Version;

import java.io.IOException;
import java.util.List;

import static com.googlecode.totallylazy.Closeables.using;

public class OptimisedStorage implements LuceneStorage {
    private final Directory directory;
    private final Version version;
    private final Analyzer analyzer;
    private final IndexWriterConfig.OpenMode mode;
    private final Object lock = new Object();

    private SearcherPool pool;
    private IndexWriter writer;

    public OptimisedStorage(Directory directory) {
        this(directory, Version.LUCENE_33, new KeywordAnalyzer(), IndexWriterConfig.OpenMode.CREATE_OR_APPEND);
    }

    public OptimisedStorage(Directory directory, Version version, Analyzer analyzer, IndexWriterConfig.OpenMode mode) {
        this.directory = directory;
        this.version = version;
        this.analyzer = analyzer;
        this.mode = mode;
    }

    @Override
    public Number add(Sequence<Document> documents) throws IOException {
        ensureIndexIsSetup();
        List<Document> docs = documents.toList();
        writer.addDocuments(docs);
        writer.commit();
        pool.markAsDirty();
        return docs.size();
    }

    @Override
    public Number delete(Query query) throws IOException {
        ensureIndexIsSetup();
        int count = count(query);
        writer.deleteDocuments(query);
        writer.commit();
        pool.markAsDirty();
        return count;
    }

    @Override
    public void deleteAll() throws IOException {
        writer.deleteAll();
        writer.commit();
        pool.markAsDirty();
    }

    @Override
    public int count(final Query query) throws IOException {
        return search(new Callable1<Searcher, Integer>() {
            @Override
            public Integer call(Searcher searcher) throws Exception {
                return searcher.search(query).totalHits;
            }
        });
    }

    @Override
    public <T> T search(Callable1<Searcher, T> callable) throws IOException {
        return using(searcher(), callable);
    }

    @Override
    public Searcher searcher() throws IOException {
        ensureIndexIsSetup();
        return pool.searcher();
    }

    @Override
    public void close() throws IOException {
        Closeables.close(pool);
        try {
            Closeables.close(writer);
        } finally {
            ensureDirectoryUnlocked();
        }
    }

    private void ensureDirectoryUnlocked() throws IOException {
        if(IndexWriter.isLocked(directory)) {
            IndexWriter.unlock(directory);
        }
    }

    private void ensureIndexIsSetup() throws IOException {
        synchronized (lock) {
            if (writer == null) {
                writer = new IndexWriter(directory, new IndexWriterConfig(version, analyzer).setOpenMode(mode));
                writer.commit();
                pool = new OptimisedPool(directory);
            }
        }
    }
}

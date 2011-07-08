package com.googlecode.totallylazy.records.lucene;

import com.googlecode.totallylazy.LazyException;
import com.googlecode.totallylazy.Sequence;
import com.googlecode.totallylazy.iterators.ReadOnlyIterator;
import com.googlecode.totallylazy.records.Keyword;
import com.googlecode.totallylazy.records.MapRecord;
import com.googlecode.totallylazy.records.Record;
import com.googlecode.totallylazy.records.lucene.mappings.Mappings;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Fieldable;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;

import java.io.IOException;
import java.util.List;

import static com.googlecode.totallylazy.Callables.first;
import static com.googlecode.totallylazy.Predicates.*;
import static com.googlecode.totallylazy.Sequences.sequence;
import static com.googlecode.totallylazy.records.memory.MemoryRecords.updateValues;

public class DocumentIterator extends ReadOnlyIterator<Record>{
    private int index = 0;
    private final IndexSearcher searcher;
    private final Mappings mappings;
    private final Sequence<Keyword> definitions;
    private final ScoreDoc[] scoreDocs;

    public DocumentIterator(final Directory directory, final Mappings mappings, final Sequence<Keyword> definitions, Query query) {
        this.mappings = mappings;
        this.definitions = definitions;
        try {
            this.searcher = new IndexSearcher(directory);
            System.out.println("query = " + query);
            TopDocs results = searcher.search(query, Integer.MAX_VALUE);
            scoreDocs = results.scoreDocs;
        } catch (IOException e) {
            throw new LazyException(e);
        }
    }

    public boolean hasNext() {
        return index < scoreDocs.length;
    }

    public Record next() {
        try {
            int docID = scoreDocs[index++].doc;
            List<Fieldable> fields = searcher.doc(docID).getFields();
            return sequence(fields).
                    map(mappings.asPair(definitions)).
                    filter(where(first(Keyword.class), is(not(Lucene.RECORD_KEY)))).
                    fold(new MapRecord(), updateValues());
        } catch (IOException e) {
            throw new LazyException(e);
        }
    }
}

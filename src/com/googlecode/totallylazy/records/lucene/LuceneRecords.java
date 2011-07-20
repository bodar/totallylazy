package com.googlecode.totallylazy.records.lucene;

import com.googlecode.totallylazy.Callable1;
import com.googlecode.totallylazy.LazyException;
import com.googlecode.totallylazy.Predicate;
import com.googlecode.totallylazy.Sequence;
import com.googlecode.totallylazy.records.AbstractRecords;
import com.googlecode.totallylazy.records.Keyword;
import com.googlecode.totallylazy.records.Record;
import com.googlecode.totallylazy.records.lucene.mappings.Mappings;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;

import java.io.IOException;
import java.io.PrintStream;

import static com.googlecode.totallylazy.numbers.Numbers.increment;
import static com.googlecode.totallylazy.records.SelectCallable.select;
import static com.googlecode.totallylazy.records.lucene.Lucene.and;
import static com.googlecode.totallylazy.records.lucene.Lucene.record;
import static com.googlecode.totallylazy.records.memory.MemoryRecords.updateValues;

public class LuceneRecords extends AbstractRecords {
    private final Directory directory;
    private final IndexWriter writer;
    private final Mappings mappings;
    private final PrintStream printStream;
    private final Lucene lucene;

    public LuceneRecords(final Directory directory, final IndexWriter writer, final Mappings mappings, final PrintStream printStream) throws IOException {
        this.directory = directory;
        this.writer = writer;
        this.mappings = mappings;
        this.printStream = printStream;
        lucene = new Lucene(this.mappings);
    }

    public Sequence<Record> get(final Keyword recordName) {
        return new RecordSequence(lucene, directory, record(recordName), mappings.asRecord(definitions(recordName)), printStream);
    }

    public boolean exists(Keyword recordName) {
        return true;
    }

    public Number add(Keyword recordName, Sequence<Keyword> fields, Sequence<Record> records) {
        try {
            Number count = 0;
            for (Document document : records.map(select(fields)).map(mappings.asDocument(recordName, definitions(recordName)))) {
                writer.addDocument(document);
                count = increment(count);
            }
            writer.commit();
            return count;
        } catch (IOException e) {
            throw new LazyException(e);
        }
    }

    public Number set(Keyword recordName, Predicate<? super Record> predicate, Sequence<Keyword> fields, Record record) {
        Sequence<Record> updated = get(recordName).filter(predicate).map(updateWithFieldsIn(record)).realise();
        Number count = remove(recordName, predicate);
        add(recordName, updated);
        return count;
    }

    private Callable1<? super Record, Record> updateWithFieldsIn(final Record record) {
        return new Callable1<Record, Record>() {
            public Record call(Record recordToUpdate) throws Exception {
                return record.fields().fold(recordToUpdate, updateValues());
            }
        };
    }

    public Number remove(Keyword recordName, Predicate<? super Record> predicate) {
        return remove(and(record(recordName), lucene.query(predicate)));
    }

    public Number remove(Keyword recordName) {
        return remove(record(recordName));
    }

    private Number remove(Query query) {
        try {
            int result = count(query);
            writer.deleteDocuments(query);
            writer.commit();
            return result;
        } catch (IOException e) {
            throw new LazyException(e);
        }
    }

    private int count(Query query) {
        try {
            return new IndexSearcher(directory).search(query, Integer.MAX_VALUE).totalHits;
        } catch (IOException e) {
            return 0;
        }
    }

}

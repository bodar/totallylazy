package com.googlecode.totallylazy.records.memory;

import static com.googlecode.totallylazy.Predicates.is;
import static com.googlecode.totallylazy.Predicates.where;
import com.googlecode.totallylazy.matchers.NumberMatcher;
import com.googlecode.totallylazy.records.AbstractRecordsTests;
import com.googlecode.totallylazy.records.Keyword;
import static com.googlecode.totallylazy.records.Keyword.keyword;
import static com.googlecode.totallylazy.records.MapRecord.record;
import static org.hamcrest.MatcherAssert.assertThat;

import com.googlecode.totallylazy.records.Records;
import org.junit.BeforeClass;
import org.junit.Test;

public class MemoryRecordsTest extends AbstractRecordsTests {
    private static final Keyword<Object> TREES = keyword("some_table");
    private static final Keyword<String> LEAFINESS = keyword("some_field", String.class);

    public Records createRecords() {
        return new MemoryRecords();
    }

    @Test
    public void willNotFailIfAskedToRemoveATableWhichHasNotBeenAddedTo() throws Exception {
        MemoryRecords records = new MemoryRecords();
        Keyword<Object> table = TREES;
        records.define(table, LEAFINESS);
        records.remove(table);
    }

    @Test
    public void allowsAddingWithoutDefiningAKeyword() throws Exception {
        MemoryRecords records = new MemoryRecords();
        records.add(TREES, record().set(LEAFINESS, "a very leafy tree"));
        assertThat(records.get(TREES).filter(where(LEAFINESS, is("a very leafy tree"))).size(), NumberMatcher.is(1));
    }

    @Test
    public void allowsRemovingWithoutDefiningAKeyword() throws Exception {
        MemoryRecords records = new MemoryRecords();
        assertThat(records.remove(TREES), NumberMatcher.is(0));
    }

    @Test
    public void allowsGettingWithoutDefiningAKeyword() throws Exception {
        MemoryRecords records = new MemoryRecords();
        assertThat(
                records.get(TREES).filter(where(LEAFINESS, is("don't be silly there are no records"))).size(),
                NumberMatcher.is(0));
    }
}

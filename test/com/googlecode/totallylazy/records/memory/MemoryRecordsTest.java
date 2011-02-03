package com.googlecode.totallylazy.records.memory;

import com.googlecode.totallylazy.records.AbstractRecordsTests;
import com.googlecode.totallylazy.records.Keyword;
import static com.googlecode.totallylazy.records.Keyword.keyword;
import org.junit.BeforeClass;
import org.junit.Test;

public class MemoryRecordsTest extends AbstractRecordsTests {
    @BeforeClass
    public static void createRecords() {
        addRecords(new MemoryRecords());
    }

    @Test
    public void willNotFailIfAskedToRemoveATableWhichHasNotBeenAddedTo() throws Exception {
        Keyword<Object> table = keyword("some_table");
        records.define(table, keyword("some_field"));
        records.remove(table);
    }
}

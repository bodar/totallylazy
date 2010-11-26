package com.googlecode.totallylazy.records.memory;

import com.googlecode.totallylazy.records.AbstractRecordsTests;
import org.junit.BeforeClass;

public class MemoryRecordsTest extends AbstractRecordsTests {
    @BeforeClass
    public static void createRecords() {
        addRecords(new MemoryRecords());
    }

}

package com.googlecode.totallylazy.records.memory;

import com.googlecode.totallylazy.records.AbstractRecordsTests;
import org.junit.BeforeClass;

import java.sql.SQLException;

public class MemoryRecordsTest extends AbstractRecordsTests {
    @BeforeClass
    public static void createRecords() throws SQLException {
        addRecords(new MemoryRecords());
    }

}

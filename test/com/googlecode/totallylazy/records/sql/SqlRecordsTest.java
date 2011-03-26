package com.googlecode.totallylazy.records.sql;

import com.googlecode.totallylazy.records.AbstractRecordsTests;
import com.googlecode.totallylazy.records.sql.mappings.Mappings;
import org.junit.BeforeClass;

import java.sql.SQLException;

import static java.sql.DriverManager.getConnection;

public class SqlRecordsTest extends AbstractRecordsTests {
    @BeforeClass
    public static void createRecords() throws SQLException, ClassNotFoundException {
        addRecords(new SqlRecords(getConnection("jdbc:hsqldb:mem:totallylazy", "SA", ""), new Mappings(), System.out));
    }
}

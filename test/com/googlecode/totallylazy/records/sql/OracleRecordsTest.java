package com.googlecode.totallylazy.records.sql;

import com.googlecode.totallylazy.records.AbstractRecordsTests;
import com.googlecode.totallylazy.records.Records;
import com.googlecode.totallylazy.records.sql.mappings.Mappings;
import org.hamcrest.CoreMatchers;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import java.sql.SQLException;

import static com.googlecode.totallylazy.records.Keyword.keyword;
import static java.lang.System.getenv;
import static java.sql.DriverManager.getConnection;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

@Ignore
public class OracleRecordsTest extends AbstractRecordsTests {
    public Records createRecords() throws Exception {
        Class.forName("oracle.jdbc.driver.OracleDriver");

        return new SqlRecords(getConnection(getenv("JDBC_URL"), getenv("JDBC_USERNAME"), getenv("JDBC_PASSWORD")), new Mappings(), System.out);
    }

    @Test
    public void supportsDBSequences() throws Exception {
        Integer integer = records.get(keyword("dual")).map(keyword("foo.nextval", Integer.class)).head();
        assertThat(integer, is(notNullValue()));
    }

}

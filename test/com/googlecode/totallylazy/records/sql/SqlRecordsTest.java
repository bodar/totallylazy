package com.googlecode.totallylazy.records.sql;

import com.googlecode.totallylazy.records.AbstractRecordsTests;
import com.googlecode.totallylazy.records.Keyword;
import com.googlecode.totallylazy.records.sql.mappings.Mappings;
import org.junit.Test;

import static com.googlecode.totallylazy.records.sql.SqlKeywords.keyword;
import static java.sql.DriverManager.getConnection;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class SqlRecordsTest extends AbstractRecordsTests<SqlRecords> {
    public SqlRecords createRecords() throws Exception {
        return new SqlRecords(getConnection("jdbc:h2:mem:totallylazy", "SA", ""), CreateTable.Enabled, new Mappings(), logger);
    }

    @Test
    public void existsReturnsFalseIfTableNotDefined() throws Exception {
        Keyword<Object> sometable = keyword("sometable");
        assertThat(records.exists(sometable), is(false));
        records.define(sometable, keyword("id", Integer.class));
        assertThat(records.exists(sometable), is(true));
    }
}

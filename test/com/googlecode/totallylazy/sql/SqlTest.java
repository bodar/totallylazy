package com.googlecode.totallylazy.sql;

import com.googlecode.totallylazy.Sequence;
import org.junit.Ignore;
import org.junit.Test;

import java.sql.Connection;
import java.sql.DriverManager;

import static com.googlecode.totallylazy.Predicates.by;
import static com.googlecode.totallylazy.Predicates.is;
import static com.googlecode.totallylazy.matchers.IterableMatcher.hasExactly;
import static com.googlecode.totallylazy.sql.Keyword.keyword;
import static org.hamcrest.MatcherAssert.assertThat;

public class SqlTest {
    @Test
    @Ignore
    public void canDoSimpleClientBasedFiltering() throws Exception {
        Connection connection = DriverManager.getConnection("jdbc:hsqldb:mem:totallylazy", "SA", "");
        Records.define(connection, keyword("user"), keyword("age", Integer.class), keyword("name", String.class));

        Sequence<Record> results = Records.records(connection, keyword("user"));
        Sequence<String> names = results.filter(by(keyword("age", Integer.class), is(10))).map(keyword("name", String.class));
        assertThat(names, hasExactly("dan"));
    }
}

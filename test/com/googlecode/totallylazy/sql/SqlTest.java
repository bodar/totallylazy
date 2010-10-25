package com.googlecode.totallylazy.sql;

import com.googlecode.totallylazy.Sequence;
import org.junit.BeforeClass;
import org.junit.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static com.googlecode.totallylazy.Predicates.is;
import static com.googlecode.totallylazy.Predicates.when;
import static com.googlecode.totallylazy.matchers.IterableMatcher.hasExactly;
import static com.googlecode.totallylazy.sql.Keyword.keyword;
import static com.googlecode.totallylazy.sql.MapRecord.record;
import static com.googlecode.totallylazy.sql.Records.define;
import static com.googlecode.totallylazy.sql.Records.insert;
import static com.googlecode.totallylazy.sql.Records.records;
import static org.hamcrest.MatcherAssert.assertThat;

public class SqlTest {
    private static final Keyword user = keyword("user");
    private static final Keyword<Integer> age = keyword("age", Integer.class);
    private static final Keyword<String> name = keyword("name", String.class);
    private static Connection connection;

    @BeforeClass
    public static void setupDatabase() throws SQLException {
        connection = DriverManager.getConnection("jdbc:hsqldb:mem:totallylazy", "SA", "");

        define(connection, user, age, name);
        insert(connection, user,
                record().set(name, "dan").set(age, 10),
                record().set(name, "matt").set(age, 12),
                record().set(name, "bob").set(age, 10));
    }

    @Test
    public void canDoSimpleServerBasedSelectWithAllColumns() throws Exception {
        Sequence<Record> results = records(connection, user);
        System.out.println("results = " + results);
    }

    @Test
    public void canDoSimpleServerBasedSelect() throws Exception {
        Sequence<Record> results = records(connection, user);
        Sequence<String> names = results.map(name);
        assertThat(names, hasExactly("dan", "matt", "bob"));
    }

    @Test
    public void canDoSimpleServerBasedFilteringAndMap() throws Exception {
        Sequence<Record> results = records(connection, user);
        Sequence<String> names = results.filter(when(age, is(10))).map(name);
        assertThat(names, hasExactly("dan", "bob"));
    }
}

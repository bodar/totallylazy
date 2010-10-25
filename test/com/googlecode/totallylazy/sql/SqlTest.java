package com.googlecode.totallylazy.sql;

import com.googlecode.totallylazy.Callable1;
import com.googlecode.totallylazy.Sequence;
import org.hamcrest.Matchers;
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
import static com.googlecode.totallylazy.sql.Records.*;
import static org.hamcrest.MatcherAssert.assertThat;

public class SqlTest {
    private static final Keyword user = keyword("user");
    private static final Keyword<Integer> age = keyword("age", Integer.class);
    private static final Keyword<String> firstName = keyword("firstName", String.class);
    private static final Keyword<String> lastName = keyword("lastName", String.class);
    private static Connection connection;

    @BeforeClass
    public static void setupDatabase() throws SQLException {
        connection = DriverManager.getConnection("jdbc:hsqldb:mem:totallylazy", "SA", "");

        define(connection, user, age, firstName, lastName);
        insert(connection, user,
                record().set(firstName, "dan").set(lastName, "bodart").set(age, 10),
                record().set(firstName, "matt").set(lastName, "savage").set(age, 12),
                record().set(firstName, "bob").set(lastName, "martin").set(age, 10));
    }

    @Test
    public void supportsSelectingAllKeywords() throws Exception {
        Sequence<Record> results = records(connection, user);
        System.out.println("results = " + results);
    }

    @Test
    public void supportsMappingASingleKeyword() throws Exception {
        Sequence<Record> results = records(connection, user);
        Sequence<String> names = results.map(firstName);
        assertThat(names, hasExactly("dan", "matt", "bob"));
    }

    @Test
    public void supportsSelectingMultipleKeywords() throws Exception {
        Sequence<Record> results = records(connection, user);
        Sequence<Record> names = results.map(keywords(firstName, lastName));
        assertThat(names.first(), Matchers.is(record().set(firstName, "dan").set(lastName, "bodart")));
    }

    private Callable1<? super Record, Record> keywords(final Keyword... keywords) {
        return new KeywordsCallable(keywords);
    }

    @Test
    public void canDoSimpleServerBasedFilteringAndMap() throws Exception {
        Sequence<Record> results = records(connection, user);
        Sequence<String> names = results.filter(when(age, is(10))).map(firstName);
        assertThat(names, hasExactly("dan", "bob"));
    }

}

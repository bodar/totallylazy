package com.googlecode.totallylazy.sql;

import com.googlecode.totallylazy.Sequence;
import com.googlecode.totallylazy.numbers.Numbers;
import org.hamcrest.Matchers;
import org.junit.BeforeClass;
import org.junit.Test;

import java.sql.DriverManager;
import java.sql.SQLException;

import static com.googlecode.totallylazy.Predicates.is;
import static com.googlecode.totallylazy.Predicates.not;
import static com.googlecode.totallylazy.Predicates.where;
import static com.googlecode.totallylazy.matchers.IterableMatcher.hasExactly;
import static com.googlecode.totallylazy.numbers.Numbers.greaterThan;
import static com.googlecode.totallylazy.numbers.Numbers.greaterThanOrEqualTo;
import static com.googlecode.totallylazy.numbers.Numbers.lessThan;
import static com.googlecode.totallylazy.numbers.Numbers.lessThanOrEqualTo;
import static com.googlecode.totallylazy.sql.Keyword.keyword;
import static com.googlecode.totallylazy.sql.KeywordsCallable.select;
import static com.googlecode.totallylazy.sql.MapRecord.record;
import static org.hamcrest.MatcherAssert.assertThat;

public class SqlTest {
    private static final Keyword user = keyword("user");
    private static final Keyword<Integer> age = keyword("age", Integer.class);
    private static final Keyword<String> firstName = keyword("firstName", String.class);
    private static final Keyword<String> lastName = keyword("lastName", String.class);
    private static Records records;

    @BeforeClass
    public static void setupDatabase() throws SQLException {
        records =  new Records(DriverManager.getConnection("jdbc:hsqldb:mem:totallylazy", "SA", ""));

        records.define(user, age, firstName, lastName);
        records.insert(user,
                record().set(firstName, "dan").set(lastName, "bodart").set(age, 10),
                record().set(firstName, "matt").set(lastName, "savage").set(age, 12),
                record().set(firstName, "bob").set(lastName, "martin").set(age, 10));
    }

    @Test
    public void supportsSelectingAllKeywords() throws Exception {
        Sequence<Record> results = records.query(user);
        assertThat(results.first(), Matchers.is(record().set(firstName, "dan").set(lastName, "bodart").set(age, 10)));
    }

    @Test
    public void supportsMappingASingleKeyword() throws Exception {
        Sequence<Record> results = records.query(user);
        Sequence<String> names = results.map(firstName);
        assertThat(names, hasExactly("dan", "matt", "bob"));
    }

    @Test
    public void supportsSelectingMultipleKeywords() throws Exception {
        Sequence<Record> results = records.query(user);
        Sequence<Record> fullNames = results.map(select(firstName, lastName));
        assertThat(fullNames.first(), Matchers.is(record().set(firstName, "dan").set(lastName, "bodart")));
    }

    @Test
    public void supportsFilteringByASingleKeyword() throws Exception {
        Sequence<Record> results = records.query(user);
        Sequence<String> names = results.filter(where(age, is(10))).map(firstName);
        assertThat(names, hasExactly("dan", "bob"));
    }

    @Test
    public void supportsFilteringByMultipleKeywords() throws Exception {
        Sequence<Record> results = records.query(user);
        Sequence<String> names = results.filter(where(age, is(10)).and(where(lastName, is("martin")))).map(firstName);
        assertThat(names, hasExactly("bob"));
    }

    @Test
    public void supportsFilteringWithLogicalOr() throws Exception {
        Sequence<Record> results = records.query(user);
        Sequence<String> names = results.filter(where(age, is(12)).or(where(lastName, is("martin")))).map(firstName);
        assertThat(names, hasExactly("matt", "bob"));
    }

    @Test
    public void supportsFilteringWithNot() throws Exception {
        Sequence<Record> results = records.query(user);
        Sequence<String> names = results.filter(where(age, is(not(10)))).map(firstName);
        assertThat(names, hasExactly("matt"));
    }

    @Test
    public void supportsFilteringWithGreaterThan() throws Exception {
        Sequence<Record> results = records.query(user);
        Sequence<String> names = results.filter(where(age, is(greaterThan(10)))).map(firstName);
        assertThat(names, hasExactly("matt"));
    }

    @Test
    public void supportsFilteringWithGreaterThanOrEqualTo() throws Exception {
        Sequence<Record> results = records.query(user);
        Sequence<String> names = results.filter(where(age, is(greaterThanOrEqualTo(12)))).map(firstName);
        assertThat(names, hasExactly("matt"));
    }

    @Test
    public void supportsFilteringWithLessThan() throws Exception {
        Sequence<Record> results = records.query(user);
        Sequence<String> names = results.filter(where(age, is(lessThan(12)))).map(firstName);
        assertThat(names, hasExactly("dan", "bob"));
    }

    @Test
    public void supportsFilteringWithLessThanOrEqualTo() throws Exception {
        Sequence<Record> results = records.query(user);
        Sequence<String> names = results.filter(where(age, is(lessThanOrEqualTo(10)))).map(firstName);
        assertThat(names, hasExactly("dan", "bob"));
    }
}
package com.googlecode.totallylazy.sql;

import com.googlecode.totallylazy.Sequence;
import com.googlecode.totallylazy.matchers.NumberMatcher;
import org.hamcrest.Matchers;
import org.junit.BeforeClass;
import org.junit.Test;

import java.sql.SQLException;
import java.util.Date;

import static com.googlecode.totallylazy.Callables.ascending;
import static com.googlecode.totallylazy.Callables.descending;
import static com.googlecode.totallylazy.Predicates.between;
import static com.googlecode.totallylazy.Predicates.greaterThan;
import static com.googlecode.totallylazy.Predicates.greaterThanOrEqualTo;
import static com.googlecode.totallylazy.Predicates.in;
import static com.googlecode.totallylazy.Predicates.is;
import static com.googlecode.totallylazy.Predicates.lessThan;
import static com.googlecode.totallylazy.Predicates.lessThanOrEqualTo;
import static com.googlecode.totallylazy.Predicates.not;
import static com.googlecode.totallylazy.Predicates.where;
import static com.googlecode.totallylazy.Strings.contains;
import static com.googlecode.totallylazy.Strings.endsWith;
import static com.googlecode.totallylazy.Strings.startsWith;
import static com.googlecode.totallylazy.dates.Dates.date;
import static com.googlecode.totallylazy.matchers.IterableMatcher.hasExactly;
import static com.googlecode.totallylazy.sql.Keyword.keyword;
import static com.googlecode.totallylazy.sql.KeywordsCallable.select;
import static com.googlecode.totallylazy.sql.MapRecord.record;
import static java.sql.DriverManager.getConnection;
import static org.hamcrest.MatcherAssert.assertThat;

public class SqlTest {
    private static final Keyword user = keyword("user");
    private static final Keyword<Integer> age = keyword("age", Integer.class);
    private static final Keyword<Date> dob = keyword("dob", Date.class);
    private static final Keyword<String> firstName = keyword("firstName", String.class);
    private static final Keyword<String> lastName = keyword("lastName", String.class);
    private static Records records;

    @BeforeClass
    public static void setupDatabase() throws SQLException {
        records = new Records(getConnection("jdbc:hsqldb:mem:totallylazy", "SA", ""));

        records.define(user, age, dob, firstName, lastName);
        records.add(user,
                record().set(firstName, "dan").set(lastName, "bodart").set(age, 10).set(dob, date(1977, 1, 10)),
                record().set(firstName, "matt").set(lastName, "savage").set(age, 12).set(dob, date(1975, 1, 10)),
                record().set(firstName, "bob").set(lastName, "martin").set(age, 11).set(dob, date(1976, 1, 10)));
    }


    @Test
    public void supportsSelectingAllKeywords() throws Exception {
        Sequence<Record> results = records.query(user);
        assertThat(results.first(), Matchers.is(record().set(firstName, "dan").set(lastName, "bodart").set(age, 10).set(dob, date(1977, 1, 10))));
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
        Sequence<String> names = results.filter(where(age, is(11))).map(firstName);
        assertThat(names, hasExactly("bob"));
    }

    @Test
    public void supportsFilteringByMultipleKeywords() throws Exception {
        Sequence<Record> results = records.query(user);
        Sequence<String> names = results.filter(where(age, is(11)).and(where(lastName, is("martin")))).map(firstName);
        assertThat(names, hasExactly("bob"));
    }

    @Test
    public void supportsFilteringWithLogicalOr() throws Exception {
        Sequence<Record> results = records.query(user);
        Sequence<String> names = results.filter(where(age, is(12)).or(where(lastName, is("martin")))).map(firstName);
        assertThat(names, hasExactly("matt", "bob"));
    }

    @Test
    public void supportsFilteringWithLogicalOrCombineWithAnd() throws Exception {
        Sequence<Record> results = records.query(user);
        Sequence<String> names = results.filter(where(age, is(12)).and(where(lastName, is("savage"))).
                or(where(firstName, is("dan")).
                or(where(lastName, is("martin"))))).map(firstName);
        assertThat(names, hasExactly("dan", "matt", "bob"));
    }

    @Test
    public void supportsFilteringWithNot() throws Exception {
        Sequence<Record> results = records.query(user);
        Sequence<String> names = results.filter(where(age, is(not(11)))).map(firstName);
        assertThat(names, hasExactly("dan", "matt"));
    }

    @Test
    public void supportsFilteringWithGreaterThan() throws Exception {
        Sequence<Record> results = records.query(user);
        Sequence<String> names = results.filter(where(age, is(greaterThan(11)))).map(firstName);
        assertThat(names, hasExactly("matt"));
    }

    @Test
    public void supportsFilteringWithDates() throws Exception {
        assertThat(records.query(user).filter(where(dob, is(date(1977, 1, 10)))).map(firstName), hasExactly("dan"));
        assertThat(records.query(user).filter(where(dob, is(greaterThan(date(1977, 1, 1))))).map(firstName), hasExactly("dan"));
        assertThat(records.query(user).filter(where(dob, is(greaterThanOrEqualTo(date(1977, 1, 10))))).map(firstName), hasExactly("dan"));
        assertThat(records.query(user).filter(where(dob, is(lessThan(date(1976, 2, 10))))).map(firstName), hasExactly("matt", "bob"));
        assertThat(records.query(user).filter(where(dob, is(lessThanOrEqualTo(date(1975, 1, 10))))).map(firstName), hasExactly("matt"));
        assertThat(records.query(user).filter(where(dob, is(between(date(1975, 6, 10), date(1976, 6, 10))))).map(firstName), hasExactly("bob"));
    }

    @Test
    public void supportsFilteringWithStrings() throws Exception {
        assertThat(records.query(user).filter(where(firstName, is(greaterThan("e")))).map(firstName), hasExactly("matt"));
        assertThat(records.query(user).filter(where(firstName, is(greaterThanOrEqualTo("dan")))).map(firstName), hasExactly("dan", "matt"));
        assertThat(records.query(user).filter(where(firstName, is(lessThan("dan")))).map(firstName), hasExactly("bob"));
        assertThat(records.query(user).filter(where(firstName, is(lessThanOrEqualTo("dan")))).map(firstName), hasExactly("dan", "bob"));
        assertThat(records.query(user).filter(where(firstName, is(between("b", "d")))).map(firstName), hasExactly("bob"));
    }

    @Test
    public void supportsFilteringWithGreaterThanOrEqualTo() throws Exception {
        Sequence<Record> results = records.query(user);
        Sequence<String> names = results.filter(where(age, is(greaterThanOrEqualTo(11)))).map(firstName);
        assertThat(names, hasExactly("matt", "bob"));
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
        assertThat(names, hasExactly("dan"));
    }

    @Test
    public void supportsSorting() throws Exception {
        Sequence<Record> results = records.query(user);
        assertThat(results.sortBy(age).map(firstName), hasExactly("dan", "bob", "matt"));
        assertThat(results.sortBy(ascending(age)).map(firstName), hasExactly("dan", "bob", "matt"));
        assertThat(results.sortBy(descending(age)).map(firstName), hasExactly("matt", "bob", "dan"));
    }

    @Test
    public void supportsSize() throws Exception {
        Sequence<Record> results = records.query(user);
        assertThat(results.size(), NumberMatcher.is(3));
    }

    @Test
    public void supportsBetween() throws Exception {
        Sequence<Record> results = records.query(user);
        assertThat(results.filter(where(age, is(between(10,11)))).map(firstName), hasExactly("dan", "bob"));
    }

    @Test
    public void supportsIn() throws Exception {
        Sequence<Record> results = records.query(user);
        assertThat(results.filter(where(age, is(in(10,12)))).map(firstName), hasExactly("dan", "matt"));
    }

    @Test
    public void supportsInWithSubSelects() throws Exception {
        Sequence<Record> results = records.query(user);
        Sequence<Integer> ages = records.query(user).filter(where(firstName, is(between("a", "e")))).map(age);
        assertThat(results.filter(where(age, is(in(ages)))).map(firstName), hasExactly("dan", "bob"));
    }

    @Test
    public void supportsStartsWith() throws Exception {
        Sequence<Record> results = records.query(user);
        assertThat(results.filter(where(firstName, startsWith("d"))).map(firstName), hasExactly("dan"));
    }

    @Test
    public void supportsContains() throws Exception {
        Sequence<Record> results = records.query(user);
        assertThat(results.filter(where(firstName, contains("a"))).map(firstName), hasExactly("dan", "matt"));
    }

    @Test
    public void supportsEndsWith() throws Exception {
        Sequence<Record> results = records.query(user);
        assertThat(results.filter(where(firstName, endsWith("b"))).map(firstName), hasExactly("bob"));
    }
}
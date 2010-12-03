package com.googlecode.totallylazy.records;

import com.googlecode.totallylazy.Predicate;
import com.googlecode.totallylazy.Sequence;
import com.googlecode.totallylazy.matchers.NumberMatcher;
import org.junit.Test;

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
import static com.googlecode.totallylazy.records.Keyword.keyword;
import static com.googlecode.totallylazy.records.MapRecord.record;
import static com.googlecode.totallylazy.records.SelectCallable.select;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.equalTo;

public abstract class AbstractRecordsTests {
    private static final Keyword user = keyword("user");
    private static final Keyword<Integer> age = keyword("age", Integer.class);
    private static final Keyword<Date> dob = keyword("dob", Date.class);
    private static final Keyword<String> firstName = keyword("firstName", String.class);
    private static final Keyword<String> lastName = keyword("lastName", String.class);
    private static Records records;

    public static void addRecords(Records records) {
        AbstractRecordsTests.records = records;

        records.define(user, age, dob, firstName, lastName);
        records.add(user,
                record().set(firstName, "dan").set(lastName, "bodart").set(age, 10).set(dob, date(1977, 1, 10)),
                record().set(firstName, "matt").set(lastName, "savage").set(age, 12).set(dob, date(1975, 1, 10)),
                record().set(firstName, "bob").set(lastName, "martin").set(age, 11).set(dob, date(1976, 1, 10)));
    }

    @Test
    public void supportsUpdating() throws Exception {
        Number count = records.set(user, where(firstName, is("dan")).and(where(age, is(10))), record().set(lastName, "bod"));
        assertThat(count, NumberMatcher.is(1));
        assertThat(records.get(user).filter(where(firstName, is("dan"))).map(lastName), hasExactly("bod"));
        records.set(user, where(firstName, is("dan")), record().set(lastName, "bodart"));
    }

    @Test
    public void supportsSelectingAllKeywords() throws Exception {
        assertThat(records.get(user).first().fields().size(), NumberMatcher.is(4));
    }

    @Test
    public void supportsMappingASingleKeyword() throws Exception {
        Sequence<String> names = records.get(user).map(firstName);
        assertThat(names, containsInAnyOrder("dan", "matt", "bob"));
    }

    @Test
    public void supportsSelectingMultipleKeywords() throws Exception {
        Sequence<Record> users = records.get(user);
        Sequence<Record> fullNames = users.map(select(firstName, lastName));
        assertThat(fullNames.first().fields().size(), NumberMatcher.is(2));
    }

    @Test
    public void supportsFilteringByASingleKeyword() throws Exception {
        Sequence<Record> users = records.get(user);
        Sequence<String> names = users.filter(where(age, is(11))).map(firstName);
        assertThat(names, hasExactly("bob"));
    }

    @Test
    public void supportsFilteringByMultipleKeywords() throws Exception {
        Sequence<Record> users = records.get(user);
        Sequence<String> names = users.filter(where(age, is(11)).and(where(lastName, is("martin")))).map(firstName);
        assertThat(names, hasExactly("bob"));
    }

    @Test
    public void supportsFilteringWithLogicalOr() throws Exception {
        Sequence<Record> users = records.get(user);
        Sequence<String> names = users.filter(where(age, is(12)).or(where(lastName, is("martin")))).map(firstName);
        assertThat(names, containsInAnyOrder("matt", "bob"));
    }

    @Test
    public void supportsFilteringWithLogicalOrCombineWithAnd() throws Exception {
        Sequence<Record> users = records.get(user);
        Sequence<String> names = users.filter(where(age, is(12)).and(where(lastName, is("savage"))).
                or(where(firstName, is("dan")).
                or(where(lastName, is("martin"))))).map(firstName);
        assertThat(names, containsInAnyOrder("dan", "matt", "bob"));
    }

    @Test
    public void supportsFilteringWithNot() throws Exception {
        Sequence<Record> users = records.get(user);
        Sequence<String> names = users.filter(where(age, is(not(11)))).map(firstName);
        assertThat(names, containsInAnyOrder("dan", "matt"));
    }

    @Test
    public void supportsFilteringWithGreaterThan() throws Exception {
        Sequence<Record> users = records.get(user);
        Sequence<String> names = users.filter(where(age, is(greaterThan(11)))).map(firstName);
        assertThat(names, containsInAnyOrder("matt"));
    }

    @Test
    public void supportsFilteringWithDates() throws Exception {
        assertThat(records.get(user).filter(where(dob, is(date(1977, 1, 10)))).map(firstName), containsInAnyOrder("dan"));
        assertThat(records.get(user).filter(where(dob, is(greaterThan(date(1977, 1, 1))))).map(firstName), containsInAnyOrder("dan"));
        assertThat(records.get(user).filter(where(dob, is(greaterThanOrEqualTo(date(1977, 1, 10))))).map(firstName), containsInAnyOrder("dan"));
        assertThat(records.get(user).filter(where(dob, is(lessThan(date(1976, 2, 10))))).map(firstName), containsInAnyOrder("matt", "bob"));
        assertThat(records.get(user).filter(where(dob, is(lessThanOrEqualTo(date(1975, 1, 10))))).map(firstName), containsInAnyOrder("matt"));
        assertThat(records.get(user).filter(where(dob, is(between(date(1975, 6, 10), date(1976, 6, 10))))).map(firstName), containsInAnyOrder("bob"));
    }

    @Test
    public void supportsFilteringWithStrings() throws Exception {
        assertThat(records.get(user).filter(where(firstName, is(greaterThan("e")))).map(firstName), containsInAnyOrder("matt"));
        assertThat(records.get(user).filter(where(firstName, is(greaterThanOrEqualTo("dan")))).map(firstName), containsInAnyOrder("dan", "matt"));
        assertThat(records.get(user).filter(where(firstName, is(lessThan("dan")))).map(firstName), containsInAnyOrder("bob"));
        assertThat(records.get(user).filter(where(firstName, is(lessThanOrEqualTo("dan")))).map(firstName), containsInAnyOrder("dan", "bob"));
        assertThat(records.get(user).filter(where(firstName, is(between("b", "d")))).map(firstName), containsInAnyOrder("bob"));
    }

    @Test
    public void supportsFilteringWithGreaterThanOrEqualTo() throws Exception {
        Sequence<Record> users = records.get(user);
        Sequence<String> names = users.filter(where(age, is(greaterThanOrEqualTo(11)))).map(firstName);
        assertThat(names, containsInAnyOrder("matt", "bob"));
    }

    @Test
    public void supportsFilteringWithLessThan() throws Exception {
        Sequence<Record> users = records.get(user);
        Sequence<String> names = users.filter(where(age, is(lessThan(12)))).map(firstName);
        assertThat(names, containsInAnyOrder("dan", "bob"));
    }

    @Test
    public void supportsFilteringWithLessThanOrEqualTo() throws Exception {
        Sequence<Record> users = records.get(user);
        Sequence<String> names = users.filter(where(age, is(lessThanOrEqualTo(10)))).map(firstName);
        assertThat(names, containsInAnyOrder("dan"));
    }

    @Test
    public void supportsSorting() throws Exception {
        Sequence<Record> users = records.get(user);
        assertThat(users.sortBy(age).map(firstName), containsInAnyOrder("dan", "bob", "matt"));
        assertThat(users.sortBy(ascending(age)).map(firstName), containsInAnyOrder("dan", "bob", "matt"));
        assertThat(users.sortBy(descending(age)).map(firstName), containsInAnyOrder("matt", "bob", "dan"));
    }

    @Test
    public void supportsSize() throws Exception {
        Sequence<Record> users = records.get(user);
        assertThat(users.size(), NumberMatcher.is(3));
    }

    @Test
    public void supportsBetween() throws Exception {
        Sequence<Record> users = records.get(user);
        assertThat(users.filter(where(age, is(between(10,11)))).map(firstName), containsInAnyOrder("dan", "bob"));
    }

    @Test
    public void supportsIn() throws Exception {
        Sequence<Record> users = records.get(user);
        assertThat(users.filter(where(age, is(in(10,12)))).map(firstName), containsInAnyOrder("dan", "matt"));
    }

    @Test
    public void supportsInWithSubSelects() throws Exception {
        Sequence<Record> users = records.get(user);
        Sequence<Integer> ages = records.get(user).filter(where(firstName, is(between("a", "e")))).map(age);
        assertThat(users.filter(where(age, is(in(ages)))).map(firstName), containsInAnyOrder("dan", "bob"));
    }

    @Test
    public void supportsStartsWith() throws Exception {
        Sequence<Record> users = records.get(user);
        assertThat(users.filter(where(firstName, startsWith("d"))).map(firstName), containsInAnyOrder("dan"));
    }

    @Test
    public void supportsContains() throws Exception {
        Sequence<Record> users = records.get(user);
        assertThat(users.filter(where(firstName, contains("a"))).map(firstName), containsInAnyOrder("dan", "matt"));
    }

    @Test
    public void supportsEndsWith() throws Exception {
        Sequence<Record> users = records.get(user);
        assertThat(users.filter(where(firstName, endsWith("b"))).map(firstName), containsInAnyOrder("bob"));
    }

    @Test
    public void supportsRemove() throws Exception {
        assertThat(records.remove(user, where(age, is(greaterThan(10)))).intValue(), equalTo(2));
        assertThat(records.remove(user, where(age, is(greaterThan(10)))).intValue(), equalTo(0));

        assertThat(records.get(user).size().intValue(), equalTo(1));
    }
}
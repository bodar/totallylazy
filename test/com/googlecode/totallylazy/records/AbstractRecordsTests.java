package com.googlecode.totallylazy.records;

import com.googlecode.totallylazy.Sequence;
import com.googlecode.totallylazy.Sequences;
import com.googlecode.totallylazy.matchers.NumberMatcher;
import com.googlecode.totallylazy.numbers.Numbers;
import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.Test;

import java.util.Date;

import static com.googlecode.totallylazy.Callables.ascending;
import static com.googlecode.totallylazy.Callables.descending;
import static com.googlecode.totallylazy.Dates.date;
import static com.googlecode.totallylazy.Predicates.between;
import static com.googlecode.totallylazy.Predicates.greaterThan;
import static com.googlecode.totallylazy.Predicates.greaterThanOrEqualTo;
import static com.googlecode.totallylazy.Predicates.in;
import static com.googlecode.totallylazy.Predicates.is;
import static com.googlecode.totallylazy.Predicates.lessThan;
import static com.googlecode.totallylazy.Predicates.lessThanOrEqualTo;
import static com.googlecode.totallylazy.Predicates.not;
import static com.googlecode.totallylazy.Predicates.notNullValue;
import static com.googlecode.totallylazy.Predicates.nullValue;
import static com.googlecode.totallylazy.Predicates.where;
import static com.googlecode.totallylazy.Strings.contains;
import static com.googlecode.totallylazy.Strings.endsWith;
import static com.googlecode.totallylazy.Strings.startsWith;
import static com.googlecode.totallylazy.matchers.IterableMatcher.hasExactly;
import static com.googlecode.totallylazy.matchers.NumberMatcher.equalTo;
import static com.googlecode.totallylazy.records.Aggregate.average;
import static com.googlecode.totallylazy.records.Aggregate.maximum;
import static com.googlecode.totallylazy.records.Aggregate.minimum;
import static com.googlecode.totallylazy.records.Aggregate.sum;
import static com.googlecode.totallylazy.records.Aggregates.to;
import static com.googlecode.totallylazy.records.CountNotNull.count;
import static com.googlecode.totallylazy.records.Keyword.keyword;
import static com.googlecode.totallylazy.records.MapRecord.record;
import static com.googlecode.totallylazy.records.SelectCallable.select;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;

public abstract class AbstractRecordsTests {
    protected static Keyword user = keyword("people");
    protected static Keyword<Integer> age = keyword("age", Integer.class);
    protected static Keyword<Date> dob = keyword("dob", Date.class);
    protected static Keyword<String> firstName = keyword("firstName", String.class);
    protected static Keyword<String> lastName = keyword("lastName", String.class);
    protected Records records;

    protected abstract Records createRecords() throws Exception;

    @Before
    public void addRecords() throws Exception {
        this.records = createRecords();
        records.remove(user);
        records.define(user, age, dob, firstName, lastName);
        addUsers(records);
    }

    private void addUsers(Records records) {
        records.add(user,
                record().set(firstName, "dan").set(lastName, "bodart").set(age, 10).set(dob, date(1977, 1, 10)),
                record().set(firstName, "matt").set(lastName, "savage").set(age, 12).set(dob, date(1975, 1, 10)),
                record().set(firstName, "bob").set(lastName, "martin").set(age, 11).set(dob, date(1976, 1, 10)));
    }

    @Test
    public void supportsIsNullAndNotNull() throws Exception {
        records.add(user, record().set(firstName, "null age").set(lastName, "").set(age, null).set(dob, date(1974, 1, 10)));
        assertThat(records.get(user).filter(where(age, is(notNullValue()))).toList().size(), NumberMatcher.is(3));
        assertThat(records.get(user).filter(where(age, is(nullValue()))).toList().size(), NumberMatcher.is(1));
    }

    @Test
    public void supportsReduce() throws Exception {
        assertThat(records.get(user).map(age).reduce(Maximum.<Integer>maximum()), CoreMatchers.is(12));
        assertThat(records.get(user).map(dob).reduce(Minimum.<Date>minimum()), CoreMatchers.is(date(1975, 1, 10)));
        assertThat(records.get(user).map(age).reduce(Numbers.sum()), NumberMatcher.is(33));
        assertThat(records.get(user).map(age).reduce(Numbers.average()), NumberMatcher.is(11));
        assertThat(records.get(user).fold(0, count()), NumberMatcher.is(3));

        records.add(user, record().set(firstName, "null age").set(lastName, "").set(age, null).set(dob, date(1974, 1, 10)));
        assertThat(records.get(user).map(age).fold(0, count()), NumberMatcher.is(3));
    }

    @Test
    public void supportsReducingMultipleValuesAtTheSameTime() throws Exception {
        Record result = records.get(user).reduce(to(maximum(age), minimum(dob), sum(age), average(age)));
        assertThat(result.get(maximum(age)), NumberMatcher.is(12));
        assertThat(result.get(minimum(dob)), CoreMatchers.is(date(1975, 1, 10)));
        assertThat(result.get(sum(age)), NumberMatcher.is(33));
        assertThat(result.get(average(age)), NumberMatcher.is(11));
    }

    @Test
    public void supportsAliasingAnAggregate() throws Exception {
        Record result = records.get(user).reduce(to(maximum(age).as(age)));
        assertThat(result.get(age), NumberMatcher.is(12));
    }

    @Test
    public void supportsSet() throws Exception {
        records.add(user, record().set(firstName, "chris").set(lastName, "bodart").set(age, 13).set(dob, date(1974, 1, 10)));
        assertThat(records.get(user).filter(where(lastName, startsWith("bod"))).map(select(lastName)).toSet(), hasExactly(record().set(lastName, "bodart")));
        assertThat(records.get(user).map(lastName).toSet(), containsInAnyOrder("bodart", "savage", "martin"));
    }

    @Test
    public void supportsUpdating() throws Exception {
        Number count = records.set(user, where(firstName, is("dan")).and(where(age, is(10))), record().set(lastName, "bod"));
        assertThat(count, NumberMatcher.is(1));
        assertThat(records.get(user).filter(where(firstName, is("dan"))).map(lastName), hasExactly("bod"));
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
                or(where(firstName, is("dan"))
                        )).map(firstName);
        assertThat(names, containsInAnyOrder("dan", "matt"));
    }

    @Test
    public void supportsFilteringWithNot() throws Exception {
        Sequence<Record> users = records.get(user);
        Sequence<String> names = users.filter(where(firstName, is(not("bob")))).map(firstName);
        assertThat(names, containsInAnyOrder("dan", "matt"));
    }

    @Test
    public void supportsFilteringWithGreaterThan() throws Exception {
        Sequence<Record> users = records.get(user);
        Sequence<String> names = users.filter(where(age, is(greaterThan(11)))).map(firstName);
        assertThat(names, hasExactly("matt"));
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
        assertThat(users.filter(where(age, is(between(10, 11)))).map(firstName), containsInAnyOrder("dan", "bob"));
    }

    @Test
    public void supportsIn() throws Exception {
        Sequence<Record> users = records.get(user);
        assertThat(users.filter(where(age, is(in(10, 12)))).map(firstName), containsInAnyOrder("dan", "matt"));
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
        assertThat(users.filter(where(firstName, startsWith("d"))).map(firstName), hasExactly("dan"));
    }

    @Test
    public void supportsContains() throws Exception {
        Sequence<Record> users = records.get(user);
        assertThat(users.filter(where(firstName, contains("a"))).map(firstName), containsInAnyOrder("dan", "matt"));
    }

    @Test
    public void supportsEndsWith() throws Exception {
        Sequence<Record> users = records.get(user);
        assertThat(users.filter(where(firstName, endsWith("n"))).map(firstName), hasExactly("dan"));
    }

    @Test
    public void supportsRemove() throws Exception {
        assertThat(records.remove(user, where(age, is(greaterThan(10)))), equalTo(2));
        assertThat(records.get(user).size(), equalTo(1));
        assertThat(records.remove(user, where(age, is(greaterThan(10)))), equalTo(0));

        assertThat(records.get(user).size(), equalTo(1));

        assertThat(records.remove(user), equalTo(1));
        assertThat(records.get(user).size(), equalTo(0));
    }

    @Test
    public void willNotFailIfAskedToAddAnEmptySequenceOfRecords() throws Exception {
        assertThat(records.add(user, new Record[0]), equalTo(0));
        assertThat(records.add(user, Sequences.<Record>sequence()), equalTo(0));
        assertThat(records.add(user, Sequences.<Keyword>sequence(), Sequences.<Record>sequence()), equalTo(0));
    }

}
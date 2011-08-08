package com.googlecode.totallylazy.records;

import com.googlecode.totallylazy.Predicates;
import com.googlecode.totallylazy.Sequence;
import com.googlecode.totallylazy.Sequences;
import com.googlecode.totallylazy.matchers.NumberMatcher;
import com.googlecode.totallylazy.numbers.Numbers;
import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.URI;
import java.util.Date;

import static com.googlecode.totallylazy.Callables.ascending;
import static com.googlecode.totallylazy.Callables.descending;
import static com.googlecode.totallylazy.Dates.date;
import static com.googlecode.totallylazy.Pair.pair;
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
import static com.googlecode.totallylazy.Streams.streams;
import static com.googlecode.totallylazy.Strings.contains;
import static com.googlecode.totallylazy.Strings.endsWith;
import static com.googlecode.totallylazy.Strings.startsWith;
import static com.googlecode.totallylazy.URLs.uri;
import static com.googlecode.totallylazy.callables.CountNotNull.count;
import static com.googlecode.totallylazy.matchers.IterableMatcher.hasExactly;
import static com.googlecode.totallylazy.matchers.NumberMatcher.equalTo;
import static com.googlecode.totallylazy.records.Aggregate.average;
import static com.googlecode.totallylazy.records.Aggregate.maximum;
import static com.googlecode.totallylazy.records.Aggregate.minimum;
import static com.googlecode.totallylazy.records.Aggregate.sum;
import static com.googlecode.totallylazy.records.Aggregates.to;
import static com.googlecode.totallylazy.records.Join.join;
import static com.googlecode.totallylazy.records.Using.using;
import static com.googlecode.totallylazy.records.Keywords.keyword;
import static com.googlecode.totallylazy.records.MapRecord.record;
import static com.googlecode.totallylazy.records.RecordMethods.update;
import static com.googlecode.totallylazy.records.SelectCallable.select;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;

public abstract class AbstractRecordsTests<T extends Records> {
    protected static Keyword people = keyword("people");
    protected static Keyword<Integer> age = keyword("age", Integer.class);
    protected static Keyword<Date> dob = keyword("dob", Date.class);
    protected static ImmutableKeyword<String> firstName = keyword("firstName", String.class);
    protected static Keyword<String> lastName = keyword("lastName", String.class);

    protected static Keyword books = keyword("books");
    protected static Keyword<URI> isbn = keyword("isbn", URI.class);
    protected static Keyword<String> title = keyword("title", String.class);

    public static final URI zenIsbn = uri("urn:isbn:0099322617");
    public static final URI godelEsherBach = uri("urn:isbn:0140289208");
    public static final URI cleanCode = uri("urn:isbn:0132350882");

    protected T records;

    protected PrintStream logger;
    private ByteArrayOutputStream stream;


    protected abstract T createRecords() throws Exception;

    @Before
    public void addRecords() throws Exception {
        stream = new ByteArrayOutputStream();
        logger = new PrintStream(streams(System.out, stream));
        this.records = createRecords();
        setupPeople();
        setupBooks();
    }

    public String log(){
        return stream.toString();
    }

    private void setupPeople() {
        records.undefine(people);
        records.define(people, age, dob, firstName, lastName, isbn);
        records.add(people,
                record().set(firstName, "dan").set(lastName, "bodart").set(age, 10).set(dob, date(1977, 1, 10)).set(isbn, zenIsbn),
                record().set(firstName, "matt").set(lastName, "savage").set(age, 12).set(dob, date(1975, 1, 10)).set(isbn, godelEsherBach),
                record().set(firstName, "bob").set(lastName, "martin").set(age, 11).set(dob, date(1976, 1, 10)).set(isbn, cleanCode));
    }

    private void setupBooks() {
        records.undefine(books);
        records.define(books, isbn, title);
        records.add(books,
                record().set(isbn, zenIsbn).set(title, "Zen And The Art Of Motorcycle Maintenance"),
                record().set(isbn, godelEsherBach).set(title, "Godel, Escher, Bach: An Eternal Golden Braid"),
                record().set(isbn, cleanCode).set(title, "Clean Code: A Handbook of Agile Software Craftsmanship"));
    }

    @Test
    public void supportsJoin() throws Exception {
        assertThat(records.get(people).filter(where(age, is(lessThan(12)))).
                flatMap(join(records.get(books), using(isbn))).
                map(title), containsInAnyOrder("Zen And The Art Of Motorcycle Maintenance", "Clean Code: A Handbook of Agile Software Craftsmanship"));
    }

    @Test
    public void supportsUri() throws Exception {
        assertThat(records.get(people).filter(where(isbn, is(zenIsbn))).realise().map(firstName), hasExactly("dan"));
    }

    @Test
    public void supportsIsNullAndNotNull() throws Exception {
        assertThat(records.add(people, record().set(firstName, "null age").set(lastName, "").set(age, null).set(dob, date(1974, 1, 10))), NumberMatcher.is(1));
        assertThat(records.get(people).filter(where(age, is(notNullValue()))).toList().size(), NumberMatcher.is(3));
        Sequence<Record> recordSequence = records.get(people);
        assertThat(recordSequence.filter(where(age, is(nullValue()))).toList().size(), NumberMatcher.is(1));
    }

    @Test
    public void supportsReduce() throws Exception {
        assertThat(records.get(people).map(age).reduce(Maximum.<Integer>maximum()), CoreMatchers.is(12));
        assertThat(records.get(people).map(dob).reduce(Minimum.<Date>minimum()), CoreMatchers.is(date(1975, 1, 10)));
        assertThat(records.get(people).map(age).reduce(Numbers.sum()), NumberMatcher.is(33));
        assertThat(records.get(people).map(age).reduce(Numbers.average()), NumberMatcher.is(11));
        assertThat(records.get(people).fold(0, count()), NumberMatcher.is(3));

        records.add(people, record().set(firstName, "null age").set(lastName, "").set(age, null).set(dob, date(1974, 1, 10)));
        assertThat(records.get(people).map(age).fold(0, count()), NumberMatcher.is(3));
    }

    @Test
    public void supportsReducingMultipleValuesAtTheSameTime() throws Exception {
        Record result = records.get(people).reduce(to(maximum(age), minimum(dob), sum(age), average(age)));
        assertThat(result.get(maximum(age)), NumberMatcher.is(12));
        assertThat(result.get(minimum(dob)), CoreMatchers.is(date(1975, 1, 10)));
        assertThat(result.get(sum(age)), NumberMatcher.is(33));
        assertThat(result.get(average(age)), NumberMatcher.is(11));
    }

    @Test
    public void supportsAliasingAnAggregate() throws Exception {
        Record result = records.get(people).reduce(to(maximum(age).as(age)));
        assertThat(result.get(age), NumberMatcher.is(12));
    }

    @Test
    public void supportsSet() throws Exception {
        records.add(people, record().set(firstName, "chris").set(lastName, "bodart").set(age, 13).set(dob, date(1974, 1, 10)));
        assertThat(records.get(people).filter(where(lastName, startsWith("bod"))).map(select(lastName)).toSet(), hasExactly(record().set(lastName, "bodart")));
        assertThat(records.get(people).map(lastName).toSet(), containsInAnyOrder("bodart", "savage", "martin"));
    }

    @Test
    public void supportsUpdating() throws Exception {
        Number count = records.set(people,
                pair(where(age, is(12)), record().set(isbn, zenIsbn)),
                pair(where(age, is(11)), record().set(isbn, zenIsbn))
        );
        assertThat(count, NumberMatcher.is(2));
        assertThat(records.get(people).filter(where(age, is(12))).map(isbn), hasExactly(zenIsbn));
        assertThat(records.get(people).filter(where(age, is(11))).map(isbn), hasExactly(zenIsbn));
    }

    @Test
    public void supportsInsertOrUpdate() throws Exception {
        URI newIsbn = uri("urn:isbn:0192861980");
        String updatedTitle = "Zen And The Art Of Motorcycle Maintenance: 25th Anniversary Edition: An Inquiry into Values";
        String newTitle = "The Emperor's New Mind: Concerning Computers, Minds, and the Laws of Physics";
        Number count = records.put(books,
                update(using(isbn),
                        record().set(isbn, zenIsbn).set(title, updatedTitle),
                        record().set(isbn, newIsbn).set(title, newTitle))
        );
        assertThat(count, NumberMatcher.is(2));
        assertThat(records.get(books).filter(where(isbn, is(zenIsbn))).map(title), hasExactly(updatedTitle));
        assertThat(records.get(books).filter(where(isbn, is(newIsbn))).map(title), hasExactly(newTitle));
    }

    @Test
    public void supportsSelectingAllKeywords() throws Exception {
        assertThat(records.get(people).first().fields().size(), NumberMatcher.is(5));
    }

    @Test
    public void supportsMappingASingleKeyword() throws Exception {
        Sequence<String> names = records.get(people).map(firstName);
        assertThat(names, containsInAnyOrder("dan", "matt", "bob"));
    }

    @Test
    public void supportsAliasingAKeyword() throws Exception {
        Keyword<String> bob = keyword("bob", String.class);
        Sequence<Keyword> fields = records.get(people).map(select(firstName.as(bob))).head().keywords();
        assertThat(fields, hasExactly(bob));
    }

    @Test
    public void supportsSelectingMultipleKeywords() throws Exception {
        Sequence<Record> users = records.get(people);
        Sequence<Record> fullNames = users.map(select(firstName, lastName));
        assertThat(fullNames.first().fields().size(), NumberMatcher.is(2));
    }

    @Test
    public void supportsFilteringByASingleKeyword() throws Exception {
        Sequence<Record> users = records.get(people);
        Sequence<String> names = users.filter(where(age, is(11))).map(firstName);
        assertThat(names, hasExactly("bob"));
    }

    @Test
    public void supportsFilteringByMultipleKeywords() throws Exception {
        Sequence<Record> users = records.get(people);
        Sequence<String> names = users.filter(where(age, is(11)).and(where(lastName, is("martin")))).map(firstName);
        assertThat(names, hasExactly("bob"));
    }

    @Test
    public void supportsFilteringWithLogicalOr() throws Exception {
        Sequence<Record> users = records.get(people);
        Sequence<String> names = users.filter(where(age, is(12)).or(where(lastName, is("martin")))).map(firstName);
        assertThat(names, containsInAnyOrder("matt", "bob"));
    }

    @Test
    public void supportsFilteringWithLogicalOrCombineWithAnd() throws Exception {
        Sequence<Record> users = records.get(people);
        Sequence<String> names = users.filter(where(age, is(12)).and(where(lastName, is("savage"))).
                or(where(firstName, is("dan"))
                )).map(firstName);
        assertThat(names, containsInAnyOrder("dan", "matt"));
    }

    @Test
    public void supportsFilteringWithNot() throws Exception {
        Sequence<Record> users = records.get(people);
        Sequence<String> names = users.filter(where(firstName, is(not("bob")))).map(firstName);
        assertThat(names, containsInAnyOrder("dan", "matt"));
    }

    @Test
    public void supportsFilteringWithGreaterThan() throws Exception {
        Sequence<Record> users = records.get(people);
        Sequence<String> names = users.filter(where(age, is(greaterThan(11)))).map(firstName);
        assertThat(names, hasExactly("matt"));
    }

    @Test
    public void supportsFilteringWithDates() throws Exception {
        assertThat(records.get(people).filter(where(dob, is(date(1977, 1, 10)))).map(firstName), containsInAnyOrder("dan"));
        assertThat(records.get(people).filter(where(dob, is(greaterThan(date(1977, 1, 1))))).map(firstName), containsInAnyOrder("dan"));
        assertThat(records.get(people).filter(where(dob, is(greaterThanOrEqualTo(date(1977, 1, 10))))).map(firstName), containsInAnyOrder("dan"));
        assertThat(records.get(people).filter(where(dob, is(lessThan(date(1976, 2, 10))))).map(firstName), containsInAnyOrder("matt", "bob"));
        assertThat(records.get(people).filter(where(dob, is(lessThanOrEqualTo(date(1975, 1, 10))))).map(firstName), containsInAnyOrder("matt"));
        assertThat(records.get(people).filter(where(dob, is(between(date(1975, 6, 10), date(1976, 6, 10))))).map(firstName), containsInAnyOrder("bob"));
    }

    @Test
    public void supportsFilteringWithStrings() throws Exception {
        assertThat(records.get(people).filter(where(firstName, is(greaterThan("e")))).map(firstName), containsInAnyOrder("matt"));
        assertThat(records.get(people).filter(where(firstName, is(greaterThanOrEqualTo("dan")))).map(firstName), containsInAnyOrder("dan", "matt"));
        assertThat(records.get(people).filter(where(firstName, is(lessThan("dan")))).map(firstName), containsInAnyOrder("bob"));
        assertThat(records.get(people).filter(where(firstName, is(lessThanOrEqualTo("dan")))).map(firstName), containsInAnyOrder("dan", "bob"));
        assertThat(records.get(people).filter(where(firstName, is(between("b", "d")))).map(firstName), containsInAnyOrder("bob"));
    }

    @Test
    public void supportsFilteringWithGreaterThanOrEqualTo() throws Exception {
        Sequence<Record> users = records.get(people);
        Sequence<String> names = users.filter(where(age, is(greaterThanOrEqualTo(11)))).map(firstName);
        assertThat(names, containsInAnyOrder("matt", "bob"));
    }

    @Test
    public void supportsFilteringWithLessThan() throws Exception {
        Sequence<Record> users = records.get(people);
        Sequence<String> names = users.filter(where(age, is(lessThan(12)))).map(firstName);
        assertThat(names, containsInAnyOrder("dan", "bob"));
    }

    @Test
    public void supportsFilteringWithLessThanOrEqualTo() throws Exception {
        Sequence<Record> users = records.get(people);
        Sequence<String> names = users.filter(where(age, is(lessThanOrEqualTo(10)))).map(firstName);
        assertThat(names, containsInAnyOrder("dan"));
    }

    @Test
    public void supportsSorting() throws Exception {
        Sequence<Record> users = records.get(people).filter(where(age, is(notNullValue())));
        assertThat(users.sortBy(age).map(firstName), containsInAnyOrder("dan", "bob", "matt"));
        assertThat(users.sortBy(ascending(age)).map(firstName), containsInAnyOrder("dan", "bob", "matt"));
        assertThat(users.sortBy(descending(age)).map(firstName), containsInAnyOrder("matt", "bob", "dan"));
    }

    @Test
    public void supportsSize() throws Exception {
        Sequence<Record> users = records.get(people);
        assertThat(users.size(), NumberMatcher.is(3));
    }

    @Test
    public void supportsBetween() throws Exception {
        Sequence<Record> users = records.get(people);
        assertThat(users.filter(where(age, is(between(10, 11)))).map(firstName), containsInAnyOrder("dan", "bob"));
    }

    @Test
    public void supportsIn() throws Exception {
        Sequence<Record> users = records.get(people);
        assertThat(users.filter(where(age, is(in(10, 12)))).map(firstName), containsInAnyOrder("dan", "matt"));
    }

    @Test
    public void supportsInWithSubSelects() throws Exception {
        Sequence<Record> users = records.get(people);
        Sequence<Integer> ages = records.get(people).filter(where(firstName, is(between("a", "e")))).map(age);
        assertThat(users.filter(where(age, is(in(ages)))).map(firstName), containsInAnyOrder("dan", "bob"));
    }

    @Test
    public void supportsStartsWith() throws Exception {
        Sequence<Record> users = records.get(people);
        assertThat(users.filter(where(firstName, startsWith("d"))).map(firstName), hasExactly("dan"));
    }

    @Test
    public void supportsContains() throws Exception {
        Sequence<Record> users = records.get(people);
        assertThat(users.filter(where(firstName, contains("a"))).map(firstName), containsInAnyOrder("dan", "matt"));
    }

    @Test
    public void supportsEndsWith() throws Exception {
        Sequence<Record> users = records.get(people);
        assertThat(users.filter(where(firstName, endsWith("n"))).map(firstName), hasExactly("dan"));
    }

    @Test
    public void supportsRemove() throws Exception {
        assertThat(records.remove(people, where(age, is(greaterThan(10)))), equalTo(2));
        assertThat(records.get(people).size(), equalTo(1));
        assertThat(records.remove(people, where(age, is(greaterThan(10)))), equalTo(0));

        assertThat(records.get(people).size(), equalTo(1));

        assertThat(records.remove(people), equalTo(1));
        assertThat(records.get(people).size(), equalTo(0));
    }

    @Test
    public void willNotFailIfAskedToAddAnEmptySequenceOfRecords() throws Exception {
        assertThat(records.add(people, new Record[0]), equalTo(0));
        assertThat(records.add(people, Sequences.<Record>sequence()), equalTo(0));
    }

}
package com.googlecode.totallylazy.sql;

import com.googlecode.totallylazy.Sequence;
import org.junit.Ignore;
import org.junit.Test;

import java.sql.Connection;
import java.sql.DriverManager;

import static com.googlecode.totallylazy.Predicates.by;
import static com.googlecode.totallylazy.Predicates.is;
import static com.googlecode.totallylazy.Predicates.when;
import static com.googlecode.totallylazy.matchers.IterableMatcher.hasExactly;
import static com.googlecode.totallylazy.sql.Keyword.keyword;
import static com.googlecode.totallylazy.sql.MapRecord.record;
import static org.hamcrest.MatcherAssert.assertThat;

public class SqlTest {
    @Test
    public void canDoSimpleClientBasedFiltering() throws Exception {
        Connection connection = DriverManager.getConnection("jdbc:hsqldb:mem:totallylazy", "SA", "");
        final Keyword user = keyword("user");
        final Keyword<Integer> age = keyword("age", Integer.class);
        final Keyword<String> name = keyword("name", String.class);

        Records.define(connection, user, age, name);

        Records.insert(connection,
                record(user).set(name, "dan").set(age, 10),
                record(user).set(name, "matt").set(age, 12),
                record(user).set(name, "bob").set(age, 10));

        Sequence<Record> results = Records.records(connection, user);
        Sequence<String> names = results.filter(when(age, is(10))).map(name);
        assertThat(names, hasExactly("dan", "bob"));
    }
}

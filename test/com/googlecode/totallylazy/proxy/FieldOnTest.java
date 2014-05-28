package com.googlecode.totallylazy.proxy;

import com.googlecode.totallylazy.Sequence;
import org.junit.Test;

import static com.googlecode.totallylazy.Sequences.sequence;
import static com.googlecode.totallylazy.matchers.IterablePredicates.hasExactly;
import static com.googlecode.totallylazy.Predicates.is;
import static com.googlecode.totallylazy.proxy.User.user;
import static com.googlecode.totallylazy.Assert.assertThat;

public class FieldOnTest {
    @Test
    public void capturesField() throws Exception {
        assertThat(new FieldOn<User, String>() { { get(instance.firstName); } }.field(), is(User.class.getField("firstName")));
    }

    @Test
    public void canMapWithAField() throws Exception {
        Sequence<User> users = sequence(user("Dan", "Bodart"), user("Matt", "Savage"));
        Sequence<String> firstNames = users.map(new FieldOn<User, String>() { { get(instance.firstName); } });
        assertThat(firstNames, hasExactly("Dan", "Matt"));
    }
}

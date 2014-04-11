package com.googlecode.totallylazy.proxy;

import com.googlecode.totallylazy.Seq;
import org.junit.Test;

import static com.googlecode.totallylazy.Sequences.sequence;
import static com.googlecode.totallylazy.matchers.IterableMatcher.hasExactly;
import static com.googlecode.totallylazy.matchers.Matchers.is;
import static com.googlecode.totallylazy.proxy.User.user;
import static org.hamcrest.MatcherAssert.assertThat;

public class FieldOnTest {
    @Test
    public void capturesField() throws Exception {
        assertThat(new FieldOn<User, String>() { { get(instance.firstName); } }.field(), is(User.class.getField("firstName")));
    }

    @Test
    public void canMapWithAField() throws Exception {
        Seq<User> users = sequence(user("Dan", "Bodart"), user("Matt", "Savage"));
        Seq<String> firstNames = users.map(new FieldOn<User, String>() { { get(instance.firstName); } });
        assertThat(firstNames, hasExactly("Dan", "Matt"));
    }
}

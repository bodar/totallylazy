package com.googlecode.totallylazy.proxy;

import com.googlecode.totallylazy.Fields;
import com.googlecode.totallylazy.Sequence;
import org.junit.Test;

import java.lang.reflect.Field;

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
        Sequence<User> users = sequence(user("Dan", "Bodart"), user("Matt", "Savage"));
        Sequence<String> firstNames = users.map(new FieldOn<User, String>() { { get(instance.firstName); } });
        assertThat(firstNames, hasExactly("Dan", "Matt"));
    }
}

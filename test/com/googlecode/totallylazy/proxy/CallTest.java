package com.googlecode.totallylazy.proxy;

import com.googlecode.totallylazy.Seq;
import org.junit.Test;

import static com.googlecode.totallylazy.Sequences.sequence;
import static com.googlecode.totallylazy.matchers.IterableMatcher.hasExactly;
import static com.googlecode.totallylazy.proxy.Call.method;
import static com.googlecode.totallylazy.proxy.Call.on;
import static com.googlecode.totallylazy.proxy.User.user;
import static org.hamcrest.MatcherAssert.assertThat;

public class CallTest {
    @Test
    public void canSortByProxy() throws Exception {
        User matt = user("Matt", "Savage");
        User dan = user("Dan", "Bodart");
        User bob = user("Bob", "Marshal");
        Seq<User> unsorted = sequence(matt, dan, bob);
        Seq<User> sorted = unsorted.sortBy(method(on(User.class).firstName()));
        assertThat(sorted, hasExactly(bob, dan, matt));
    }

    @Test
    public void canMapAMethod() throws Exception {
        Seq<User> users = sequence(user("Dan", "Bodart"), user("Matt", "Savage"));
        Seq<String> firstNames = users.map(method(on(User.class).firstName()));
        assertThat(firstNames, hasExactly("Dan", "Matt"));
    }

    @Test
    public void canMapAMethodWithAnArgument() throws Exception {
        Seq<User> users = sequence(user("Dan", "Bodart"), user("Matt", "Savage"));
        Seq<String> firstNames = users.map(method(on(User.class).say("Hello")));
        assertThat(firstNames, hasExactly("Dan says 'Hello'", "Matt says 'Hello'"));
    }

}

package com.googlecode.totallylazy.proxy;

import com.googlecode.totallylazy.Sequence;
import org.junit.Test;

import static com.googlecode.totallylazy.Sequences.sequence;
import static com.googlecode.totallylazy.matchers.IterablePredicates.hasExactly;
import static com.googlecode.totallylazy.proxy.User.user;
import static com.googlecode.totallylazy.PredicateAssert.assertThat;
public class CallOnTest {
    @Test
    public void canUseInstanceInsteadOfCallForReadability() throws Exception {
        Sequence<User> users = sequence(user("Dan", "Bodart"), user("Matt", "Savage"));
        Sequence<String> firstNames = users.map(new CallOn<User, String>(){{instance.firstName();}});
        assertThat(firstNames, hasExactly("Dan", "Matt"));
    }

    @Test
    public void canSortByProxy() throws Exception {
        User matt = user("Matt", "Savage");
        User dan = user("Dan", "Bodart");
        User bob = user("Bob", "Marshal");
        Sequence<User> unsorted = sequence(matt, dan, bob);
        Sequence<User> sorted = unsorted.sortBy(new CallOn<User, String>(){{call.firstName();}});
        assertThat(sorted, hasExactly(bob, dan, matt));
    }

    @Test
    public void canMapAMethod() throws Exception {
        Sequence<User> users = sequence(user("Dan", "Bodart"), user("Matt", "Savage"));
        Sequence<String> firstNames = users.map(new CallOn<User, String>(){{call.firstName();}});
        assertThat(firstNames, hasExactly("Dan", "Matt"));
    }

    @Test
    public void canMapAMethodWithAnArgument() throws Exception {
        Sequence<User> users = sequence(user("Dan", "Bodart"), user("Matt", "Savage"));
        Sequence<String> firstNames = users.map(new CallOn<User, String>(){{call.say("Hello");}});
        assertThat(firstNames, hasExactly("Dan says 'Hello'", "Matt says 'Hello'"));
    }
}
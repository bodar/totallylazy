package com.googlecode.totallylazy.proxy;

import com.googlecode.totallylazy.Sequence;
import org.junit.Test;

import static com.googlecode.totallylazy.IterableMatcher.hasExactly;
import static com.googlecode.totallylazy.Sequences.sequence;
import static com.googlecode.totallylazy.proxy.Call.on;
import static com.googlecode.totallylazy.proxy.Call.method;
import static com.googlecode.totallylazy.proxy.CallTest.User.user;
import static org.junit.Assert.assertThat;

public class CallTest {
    @Test
    public void canSortByProxy() throws Exception {
        User matt = user("Matt", "Savage");
        User dan = user("Dan", "Bodart");
        User bob = user("Bob", "Marshal");
        Sequence<User> unsorted = sequence(matt, dan, bob);
        Sequence<User> sorted = unsorted.sortBy(Call.<User, String>method(on(User.class).firstName()));
        assertThat(sorted, hasExactly(bob, dan, matt));
    }

    @Test
    public void canAlternateInitaliseMechanism() throws Exception {
        User matt = user("Matt", "Savage");
        User dan = user("Dan", "Bodart");
        User bob = user("Bob", "Marshal");
        Sequence<User> unsorted = sequence(matt, dan, bob);
        Sequence<User> sorted = unsorted.sortBy(new Call<User, String>(){{method.firstName();}});
        assertThat(sorted, hasExactly(bob, dan, matt));
    }

    @Test
    public void canMapAMethod() throws Exception {
        Sequence<User> users = sequence(user("Dan", "Bodart"), user("Matt", "Savage"));
        Sequence<String> firstNames = users.map(method(on(User.class).firstName()));
        assertThat(firstNames, hasExactly("Dan", "Matt"));
    }

    @Test
    public void canMapAMethodWithAnArgument() throws Exception {
        Sequence<User> users = sequence(user("Dan", "Bodart"), user("Matt", "Savage"));
        Sequence<String> firstNames = users.map(method(on(User.class).say("Hello")));
        assertThat(firstNames, hasExactly("Dan says 'Hello'", "Matt says 'Hello'"));
    }

    public static class User {
        private final String firstName;
        private final String lastName;

        private User(String firstName, String lastName) {
            this.firstName = firstName;
            this.lastName = lastName;
        }

        public String firstName() {
            return firstName;
        }

        public String lastName() {
            return lastName;
        }

        public String say(String value){
            return firstName + " says '" + value + "'";
        }

        public static User user(String firstName, String lastName) {
            return new User(firstName, lastName);
        }
    }

}

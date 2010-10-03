package com.googlecode.totallylazy.proxy;

import com.googlecode.totallylazy.Sequence;
import org.junit.Test;

import static com.googlecode.totallylazy.IterableMatcher.hasExactly;
import static com.googlecode.totallylazy.Sequences.sequence;
import static com.googlecode.totallylazy.proxy.CallableProxy.on;
import static com.googlecode.totallylazy.proxy.CallableProxy.method;
import static com.googlecode.totallylazy.proxy.CallableProxyTest.User.user;
import static org.junit.Assert.assertThat;

public class CallableProxyTest {
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

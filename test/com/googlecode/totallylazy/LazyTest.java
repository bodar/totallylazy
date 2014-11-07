package com.googlecode.totallylazy;

import org.junit.Test;

import static com.googlecode.totallylazy.matchers.Matchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class LazyTest {
    @Test
    public void canCreateALazyValue() throws Exception {
        final int[] count = {0};

        Lazy<String> lazy = new Lazy<String>() {
            protected String get() { return "Hello " + ++count[0]; }
        };

        assertThat(lazy.value(), is("Hello 1"));
        assertThat(lazy.value(), is("Hello 1"));
    }

    @Test
    public void isCloseable() throws Exception {
        final int[] count = {0};

        Lazy<String> lazy = new Lazy<String>() {
            protected String get() { return "Hello " + ++count[0]; }
        };

        lazy.close();

        assertThat(count[0], is(0));

        lazy.value();
        lazy.value();
        lazy.value();

        assertThat(count[0], is(1));

        lazy.close();

        lazy.value();
        lazy.value();
        lazy.value();

        assertThat(count[0], is(2));
    }

}

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

}

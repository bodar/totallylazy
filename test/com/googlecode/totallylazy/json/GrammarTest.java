package com.googlecode.totallylazy.json;

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

public class GrammarTest {
    @Test
    public void canParseNull() throws Exception {
        assertThat(Grammar.NULL.parse("null").value(), is(nullValue()));
        assertThat(Grammar.NULL.parse("foo").failure(), is(true));
    }
}

package com.googlecode.totallylazy.records;

import org.junit.Test;

import static com.googlecode.totallylazy.records.Keyword.keyword;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class KeywordTest {
    @Test
    public void nameReturnsLastPartOfKeyword() throws Exception {
        assertThat(keyword("USER.foo").name(), is("foo"));
        assertThat(keyword("USER.TABLE.foo").name(), is("foo"));
        assertThat(keyword("foo").name(), is("foo"));
    }

    @Test
    public void equalityIsBasedOnTheNameOnly() throws Exception {
        assertThat(keyword("USER.foo").name(), is(equalTo(keyword("foo").name())));
        assertThat(keyword("USER.TABLE.foo").name(), is(equalTo(keyword("foo").name())));
        assertThat(keyword("USER.TABLE.foo").name(), is(equalTo(keyword("USER.foo").name())));
    }
}

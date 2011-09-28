package com.googlecode.totallylazy.records.sql;

import com.googlecode.totallylazy.records.AliasedKeyword;
import org.junit.Test;

import static com.googlecode.totallylazy.records.sql.SqlKeywords.keyword;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;

public class SqlKeywordTest {
    @Test
    public void ifNameContainsADotThenItReturnsAnAlias() throws Exception {
        assertThat(keyword("USER.foo"), is(instanceOf(AliasedKeyword.class)));
        assertThat(keyword("USER.TABLE.foo"), is(instanceOf(AliasedKeyword.class)));
        assertThat(keyword("foo"), is(not(instanceOf(AliasedKeyword.class))));
    }

    @Test
    public void nameReturnsLastPartOfKeyword() throws Exception {
        assertThat(keyword("USER.foo").name(), is("foo"));
        assertThat(((AliasedKeyword<Object>) keyword("USER.foo")).source().name(), is("USER.foo"));
        assertThat(keyword("USER.TABLE.foo").name(), is("foo"));
        assertThat(((AliasedKeyword<Object>) keyword("USER.TABLE.foo")).source().name(), is("USER.TABLE.foo"));
        assertThat(keyword("foo").name(), is("foo"));
    }

    @Test
    public void equalityIsBasedOnAliasNameOnly() throws Exception {
        assertThat(keyword("USER.foo"), is(equalTo(keyword("foo"))));
        assertThat(keyword("USER.TABLE.foo"), is(equalTo(keyword("foo"))));
        assertThat(keyword("USER.TABLE.foo"), is(equalTo(keyword("USER.foo"))));
        assertThat(keyword("FOO"), is(equalTo(keyword("foo"))));
    }
}

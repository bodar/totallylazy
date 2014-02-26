package com.googlecode.totallylazy.json;

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

public class GrammarTest {
    @Test
    public void canParseNull() throws Exception {
        assertThat(Grammar.NULL.parse("null").value(), is(nullValue()));
        assertThat(Grammar.NULL.parse("falure").failure(), is(true));
    }

    @Test
    public void canParseABoolean() throws Exception {
        assertThat(Grammar.BOOLEAN.parse("true").value(), is(true));
        assertThat(Grammar.BOOLEAN.parse("false").value(), is(false));
        assertThat(Grammar.BOOLEAN.parse("falure").failure(), is(true));
    }

    @Test
    public void canHandleEscapedCharacters() throws Exception {
        assertThat(Grammar.ESCAPED_CHARACTER.parse("\\\"").value(), is("\""));
        assertThat(Grammar.ESCAPED_CHARACTER.parse("\\\\").value(), is("\\"));
        assertThat(Grammar.ESCAPED_CHARACTER.parse("\\/").value(), is("/"));
        assertThat(Grammar.ESCAPED_CHARACTER.parse("\\b").value(), is("\b"));
        assertThat(Grammar.ESCAPED_CHARACTER.parse("\\f").value(), is("\f"));
        assertThat(Grammar.ESCAPED_CHARACTER.parse("\\n").value(), is("\n"));
        assertThat(Grammar.ESCAPED_CHARACTER.parse("\\r").value(), is("\r"));
        assertThat(Grammar.ESCAPED_CHARACTER.parse("\\t").value(), is("\t"));
        assertThat(Grammar.ESCAPED_CHARACTER.parse("\\u03BB").value(), is("λ"));
    }
}

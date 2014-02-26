package com.googlecode.totallylazy.json;

import com.googlecode.totallylazy.Pair;
import com.googlecode.totallylazy.matchers.NumberMatcher;
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
        assertThat(Grammar.ESCAPED_CHARACTER.parse("falure").failure(), is(true));
    }

    @Test
    public void canParseString() throws Exception {
        assertThat(Grammar.STRING.parse("\"Word\"").value(), is("Word"));
        assertThat(Grammar.STRING.parse("\"This is some \\\" random string\"").value(), is("This is some \" random string"));
        assertThat(Grammar.STRING.parse("\"Text with unicode \\u03BB\"").value(), is("Text with unicode λ"));
        assertThat(Grammar.STRING.parse("falure").failure(), is(true));
    }

    @Test
    public void canParseNumber() throws Exception {
        assertThat(Grammar.NUMBER.parse("12").value(), NumberMatcher.is(12));
        assertThat(Grammar.NUMBER.parse("12.1").value(), NumberMatcher.is(12.1));
        assertThat(Grammar.NUMBER.parse("-12").value(), NumberMatcher.is(-12));
    }

    @Test
    public void canParsePair() throws Exception {
        Pair<String, Object> pair = Grammar.PAIR.parse("\"foo\":\"value\"").value();
        assertThat(pair.first(), is("foo"));
        assertThat((String) pair.second(), is("value"));
        Pair<String, Object> parse = Grammar.PAIR.parse("\"foo\":123").value();
        assertThat(parse.first(), is("foo"));
        assertThat((Number) parse.second(), NumberMatcher.is(123));
    }

}

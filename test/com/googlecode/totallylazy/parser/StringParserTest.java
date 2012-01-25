package com.googlecode.totallylazy.parser;

import org.junit.Test;

import static com.googlecode.totallylazy.Sequences.characters;
import static com.googlecode.totallylazy.Sequences.empty;
import static com.googlecode.totallylazy.matchers.Matchers.is;
import static com.googlecode.totallylazy.parser.StringParser.string;
import static org.hamcrest.MatcherAssert.assertThat;

public class StringParserTest {
    @Test
    public void canParseAString() throws Exception {
        Success<String> result = (Success<String>) string("ABC").parse(characters("ABC"));
        assertThat(result.value(), is("ABC"));
        assertThat(result.remainder(), is(empty(Character.class)));
    }
    @Test
    public void supportsRemainder() throws Exception {
        Success<String> result = (Success<String>) string("ABC").parse(characters("ABCDEF"));
        assertThat(result.value(), is("ABC"));
        assertThat(result.remainder(), is(characters("DEF")));
    }
}

package com.googlecode.totallylazy.parser;

import org.junit.Test;

import static com.googlecode.totallylazy.Sequences.characters;
import static com.googlecode.totallylazy.Unchecked.cast;
import static com.googlecode.totallylazy.matchers.Matchers.is;
import static com.googlecode.totallylazy.parser.DisjunctiveParser.or;
import static com.googlecode.totallylazy.parser.StringParser.string;
import static org.hamcrest.MatcherAssert.assertThat;

public class DisjunctiveParserTest {
    @Test
    public void canMatchOneParser() throws Exception {
        DisjunctiveParser<String> parser = or(string("foo"), string("bar"));
        Success<String> result = cast(parser.call(characters("food")));
        assertThat(result.value(), is("foo"));
        assertThat(result.remainder(), is(characters("d")));
    }

    @Test
    public void supportsChaining() throws Exception {
        DisjunctiveParser<String> parser = string("foo").or(string("bar"));
        Success<String> result1 = cast(parser.call(characters("bart")));
        assertThat(result1.value(), is("bar"));
        assertThat(result1.remainder(), is(characters("t")));
    }
}

package com.googlecode.totallylazy.parser;

import org.junit.Test;

import static com.googlecode.totallylazy.Segment.constructors.characters;
import static com.googlecode.totallylazy.Unchecked.cast;
import static com.googlecode.totallylazy.matchers.Matchers.is;
import static com.googlecode.totallylazy.parser.OrParser.or;
import static com.googlecode.totallylazy.parser.StringParser.string;
import static org.hamcrest.MatcherAssert.assertThat;

public class OrParserTest {
    @Test
    public void canMatchOneParser() throws Exception {
        OrParser<String> parser = or(string("foo"), string("bar"));
        Success<String> result = cast(parser.parse(characters("food")));
        assertThat(result.value(), is("foo"));
        assertThat(result.remainder(), is(characters("d")));
    }

    @Test
    public void supportsChaining() throws Exception {
        Parser<String> parser = string("foo").or(string("bar"));
        Success<String> result1 = cast(parser.parse(characters("bart")));
        assertThat(result1.value(), is("bar"));
        assertThat(result1.remainder(), is(characters("t")));
    }
}

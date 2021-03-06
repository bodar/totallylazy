package com.googlecode.totallylazy.parser;

import org.junit.Test;

import static com.googlecode.totallylazy.Segment.constructors.characters;
import static com.googlecode.totallylazy.matchers.Matchers.is;
import static com.googlecode.totallylazy.parser.Parsers.or;
import static com.googlecode.totallylazy.parser.StringParser.string;
import static org.hamcrest.MatcherAssert.assertThat;

public class OrParserTest {
    @Test
    public void canMatchOneParser() throws Exception {
        Parser<String> parser = or(string("foo"), string("bar"));
        Result<String> result = parser.parse("food");
        assertThat(result.value(), is("foo"));
        assertThat(result.remainder(), is(characters("d")));
    }

    @Test
    public void supportsChaining() throws Exception {
        Parser<String> parser = string("foo").or(string("bar"));
        Result<String> result1 = parser.parse(characters("bart"));
        assertThat(result1.value(), is("bar"));
        assertThat(result1.remainder(), is(characters("t")));
    }
}

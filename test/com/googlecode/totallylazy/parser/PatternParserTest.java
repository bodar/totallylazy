package com.googlecode.totallylazy.parser;

import org.junit.Test;

import static com.googlecode.totallylazy.Segment.constructors.characters;
import static com.googlecode.totallylazy.Unchecked.cast;
import static com.googlecode.totallylazy.matchers.Matchers.is;
import static com.googlecode.totallylazy.parser.PatternParser.pattern;
import static org.hamcrest.MatcherAssert.assertThat;

public class PatternParserTest {
    @Test
    public void canParseUsingRegex() throws Exception {
        Success<String> result = cast(pattern("\\d{4}/\\d{1,2}/\\d{1,2}").parse(characters("1977/1/10ABC")));
        assertThat(result.value(), is("1977/1/10"));
        assertThat(result.remainder(), is(characters("ABC")));
    }
}

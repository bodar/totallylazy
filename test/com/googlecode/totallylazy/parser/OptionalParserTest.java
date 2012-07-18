package com.googlecode.totallylazy.parser;

import com.googlecode.totallylazy.Option;
import org.junit.Ignore;
import org.junit.Test;

import static com.googlecode.totallylazy.Option.none;
import static com.googlecode.totallylazy.Option.some;
import static com.googlecode.totallylazy.Segment.constructors.characters;
import static com.googlecode.totallylazy.Segment.constructors.emptySegment;
import static com.googlecode.totallylazy.Unchecked.cast;
import static com.googlecode.totallylazy.matchers.Matchers.is;
import static com.googlecode.totallylazy.parser.OptionalParser.optional;
import static com.googlecode.totallylazy.parser.StringParser.string;
import static org.hamcrest.MatcherAssert.assertThat;

public class OptionalParserTest {
    @Test
    public void isOptional() throws Exception {
        OptionalParser<String> parser = optional(string("foo"));
        Success<Option<String>> result = cast(parser.parse(characters("foo")));
        assertThat(result.value(), is(some("foo")));
        assertThat(result.remainder(), is(emptySegment(Character.class)));
    }

    @Test
    public void canChain() throws Exception {
        Parser<Option<String>> parser = string("foo").optional();
        Success<Option<String>> result = cast(parser.parse(characters("bob")));
        assertThat(result.value(), is(none(String.class)));
        assertThat(result.remainder(), is(characters("bob")));
    }
}

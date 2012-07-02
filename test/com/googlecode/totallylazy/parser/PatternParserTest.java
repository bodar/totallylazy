package com.googlecode.totallylazy.parser;

import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import static com.googlecode.totallylazy.Segment.constructors.characters;
import static com.googlecode.totallylazy.Segment.constructors.emptySegment;
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

    @Test
    public void canParseToEnd() throws Exception {
        Success<String> result = cast(pattern("\\d{4}/\\d{1,2}/\\d{1,2}").parse(characters("1977/1/10")));
        assertThat(result.value(), is("1977/1/10"));
        assertThat(result.remainder(), is(emptySegment(Character.class)));
    }

    @Test
    public void doesNotReadMoreThanItNeeds() throws Exception {
        InputStream stream = new ByteArrayInputStream("1977/1/10ABC".getBytes("UTF-8"));
        Reader reader = new InputStreamReader(stream, "UTF-8");
        Success<String> result = (Success<String>) pattern("\\d{4}/\\d{1,2}/\\d{1,2}").parse(characters(reader));
        assertThat(result.value(), is("1977/1/10"));
        char next = (char) reader.read();
        assertThat(next, is('A'));
    }


    @Test
    public void doesNotBlow() throws Exception {
        Success<String> result = cast(pattern("[a-zA-Z][_0-9a-zA-Z]*").parse(characters("hello")));
        assertThat(result.value(), is("hello"));
        assertThat(result.remainder(), is(emptySegment(Character.class)));
    }
}

package com.googlecode.totallylazy.parser;

import org.junit.Ignore;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.Reader;

import static com.googlecode.totallylazy.Segment.constructors.characters;
import static com.googlecode.totallylazy.Segment.constructors.emptySegment;
import static com.googlecode.totallylazy.Streams.inputStreamReader;
import static com.googlecode.totallylazy.Strings.bytes;
import static com.googlecode.totallylazy.matchers.Matchers.is;
import static com.googlecode.totallylazy.parser.PatternParser.pattern;
import static org.hamcrest.MatcherAssert.assertThat;

public class PatternParserTest {
    @Test
    public void canParseUsingRegex() throws Exception {
        Result<String> result = pattern("\\d{4}/\\d{1,2}/\\d{1,2}").parse("1977/1/10ABC");
        assertThat(result.value(), is("1977/1/10"));
        assertThat(result.remainder().toString(), is("ABC"));
    }

    @Test
    public void canParseToEnd() throws Exception {
        Result<String> result = pattern("\\d{4}/\\d{1,2}/\\d{1,2}").parse("1977/1/10");
        assertThat(result.value(), is("1977/1/10"));
        assertThat(result.remainder().toString(), is(""));
    }

    @Test
    @Ignore
    public void doesNotReadMoreThanItNeeds() throws Exception {
        InputStream stream = new ByteArrayInputStream(bytes("1977/1/10ABC"));
        Reader reader = inputStreamReader(stream);
        Success<String> result = (Success<String>) pattern("\\d{4}/\\d{1,2}/\\d{1,2}").parse((CharSequence) reader);
        assertThat(result.value(), is("1977/1/10"));
        char next = (char) reader.read();
        assertThat(next, is('A'));
    }

    @Test
    public void canWorkWithPatternsEndingWithStar() throws Exception {
        Result<String> result = pattern("[a-zA-Z][_0-9a-zA-Z]*").parse("hello");
        assertThat(result.value(), is("hello"));
        assertThat(result.remainder().toString(), is(""));
    }

    @Test
    public void doesNotConsumeMoreThanItShouldWithStar() throws Exception {
        Result<String> result = pattern("[a-zA-Z][_0-9a-zA-Z]*").parse("hello world");
        assertThat(result.value(), is("hello"));
        assertThat(result.remainder().toString(), is(" world"));
    }
}
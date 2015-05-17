package com.googlecode.totallylazy.parser;

import org.junit.Ignore;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import static com.googlecode.totallylazy.Assert.assertTrue;
import static com.googlecode.totallylazy.Segment.constructors.characters;
import static com.googlecode.totallylazy.Segment.constructors.emptySegment;
import static com.googlecode.totallylazy.Strings.UTF8;
import static com.googlecode.totallylazy.Strings.bytes;
import static com.googlecode.totallylazy.matchers.Matchers.is;
import static com.googlecode.totallylazy.parser.Parsers.pattern;
import static org.hamcrest.MatcherAssert.assertThat;

public class PatternParserTest {
    @Test
    public void supportsPrettyFailureMessage() throws Exception {
        Result<String> result = pattern("\\d{4}/\\d{1,2}/\\d{1,2}", "dddd/MM/yy").parse("ABC");
        assertThat(result.message(), is("dddd/MM/yy expected, A encountered."));
    }

    @Test
    public void canParseUsingRegex() throws Exception {
        Result<String> result = pattern("\\d{4}/\\d{1,2}/\\d{1,2}").parse("1977/1/10ABC");
        assertThat(result.value(), is("1977/1/10"));
        assertThat(result.remainder(), is(characters("ABC")));
    }

    @Test
    public void canParseToEnd() throws Exception {
        Result<String> result = pattern("\\d{4}/\\d{1,2}/\\d{1,2}").parse("1977/1/10");
        assertThat(result.value(), is("1977/1/10"));
        assertThat(result.remainder(), is(emptySegment(Character.class)));
    }

    @Test
    public void doesNotReadMoreThanItNeeds() throws Exception {
        InputStream stream = new ByteArrayInputStream(bytes("1977/1/10ABC"));
        Reader reader = new InputStreamReader(stream, UTF8);
        Success<String> result = (Success<String>) pattern("\\d{4}/\\d{1,2}/\\d{1,2}").parse(reader);
        assertThat(result.value(), is("1977/1/10"));
        char next = (char) reader.read();
        assertThat(next, is('A'));
    }

    @Test
    @Ignore("Not sure if this possible")
    public void canWorkWithPatternsEndingWithStar() throws Exception {
        Result<String> result = pattern("[a-zA-Z][_0-9a-zA-Z]*").parse("hello");
        assertThat(result.value(), is("hello"));
        assertThat(result.remainder(), is(emptySegment(Character.class)));
    }

    @Test
    public void doesnNotBlowIfPatternDoesNotCompletelyMatch() throws Exception {
        Parser<String> pattern = pattern("\\d{4}/\\d{1,2}/\\d{1,2}", "yyyyMMdd");
        Result<String> result = pattern.parse("13");
        assertThat(result.failure(), is(true));
        assertThat(result.message(), is("yyyyMMdd expected, 13 encountered."));
        assertTrue(pattern.parse("1977/01/10").success());
    }

    @Test
    public void doesNotConsumeMoreThanItShouldWithStar() throws Exception {
        Result<String> result = pattern("[a-zA-Z][_0-9a-zA-Z]*").parse("hello world");
        assertThat(result.value(), is("hello"));
        assertThat(result.remainder(), is(characters(" world")));
    }
}
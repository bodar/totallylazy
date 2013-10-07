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
import static com.googlecode.totallylazy.parser.Parsers.isChar;
import static com.googlecode.totallylazy.parser.StringParser.string;
import static org.hamcrest.MatcherAssert.assertThat;

public class StringParserTest {
    @Test
    public void canComposeCharacterParsers() throws Exception {
        Result<String> result = string(isChar('A'), isChar('B')).parse("ABC");
        assertThat(result.value(), is("AB"));
        assertThat(result.remainder().toString(), is("C"));
    }

    @Test
    public void canParseAString() throws Exception {
        Result<String> result = string("ABC").parse("ABC");
        assertThat(result.value(), is("ABC"));
        assertThat(result.remainder().toString(), is(""));
    }

    @Test
    public void supportsRemainder() throws Exception {
        Result<String> result = string("ABC").parse("ABCDEF");
        assertThat(result.value(), is("ABC"));
        assertThat(result.remainder().toString(), is("DEF"));
    }

    @Test
    @Ignore
    public void doesNotReadMoreThanItNeeds() throws Exception {
        InputStream stream = new ByteArrayInputStream(bytes("ABCDEF"));
        Reader reader = inputStreamReader(stream);
        Result<String> result = string("ABC").parse((CharSequence) reader);
        assertThat(result.value(), is("ABC"));
        char next = (char) reader.read();
        assertThat(next, is('D'));
    }
}

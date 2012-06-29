package com.googlecode.totallylazy.parser;

import com.googlecode.totallylazy.Segment;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import static com.googlecode.totallylazy.Segment.constructors.characters;
import static com.googlecode.totallylazy.Segment.constructors.emptySegment;
import static com.googlecode.totallylazy.Unchecked.cast;
import static com.googlecode.totallylazy.matchers.Matchers.is;
import static com.googlecode.totallylazy.parser.CharacterParser.character;
import static com.googlecode.totallylazy.parser.ManyParser.many;
import static org.hamcrest.MatcherAssert.assertThat;

public class ManyParserTest {
    @Test
    public void doesNotThrowIfItConsumesAllCharacters() throws Exception {
        Success<Segment<Character>> result = cast(many(character('C')).parse(characters("CCCCC")));
        assertThat(result.value(), is(characters("CCCCC")));
        assertThat(result.remainder(), is(emptySegment(Character.class)));
    }

    @Test
    public void supportMany() throws Exception {
        Success<Segment<Character>> result = cast(many(character('C')).parse(characters("CCCCCDEFG")));
        assertThat(result.value(), is(characters("CCCCC")));
        assertThat(result.remainder(), is(characters("DEFG")));
    }

    @Test
    public void supportChaining() throws Exception {
        Success<Segment<Character>> result = cast(character('C').many().parse(characters("CCCCCDEFG")));
        assertThat(result.value(), is(characters("CCCCC")));
        assertThat(result.remainder(), is(characters("DEFG")));
    }

    @Test
    public void canLazilyParseAStream() throws Exception {
        InputStream stream = new ByteArrayInputStream("AABCDEFG".getBytes("UTF-8"));
        Reader reader = new BufferedReader(new InputStreamReader(stream, "UTF-8"));
        Success<Segment<Character>> result = (Success<Segment<Character>>) character('A').many().parse(characters(reader));
        assertThat(result.value().head(), is('A'));
        assertThat(peak(reader), is('A'));
        assertThat(result.value().tail().head(), is('A'));
        assertThat(peak(reader), is('B'));
        assertThat(result.value().tail().tail().isEmpty(), is(true));
        assertThat(peak(reader), is('C'));
        assertThat(result.remainder().head(), is('B'));
    }

    @Test
    public void doesNotReadMoreThanItNeedsTo() throws Exception {
        InputStream stream = new ByteArrayInputStream("AABCDEFG".getBytes("UTF-8"));
        Reader reader = new BufferedReader(new InputStreamReader(stream, "UTF-8"));
        Success<Segment<Character>> result = (Success<Segment<Character>>) character('A').many().parse(characters(reader));
        assertThat(result.value().head(), is('A'));
        assertThat(peak(reader), is('A'));
    }

    public static char peak(Reader reader) throws IOException {
        reader.mark(1);
        char next = (char) reader.read();
        reader.reset();
        return next;
    }
}

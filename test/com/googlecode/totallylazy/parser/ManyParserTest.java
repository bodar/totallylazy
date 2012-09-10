package com.googlecode.totallylazy.parser;

import com.googlecode.totallylazy.Segment;
import org.junit.Test;

import java.io.IOException;
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

    public static char peak(Reader reader) throws IOException {
        reader.mark(1);
        char next = (char) reader.read();
        reader.reset();
        return next;
    }
}

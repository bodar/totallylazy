package com.googlecode.totallylazy.parser;

import com.googlecode.totallylazy.Segment;
import com.googlecode.totallylazy.Sequence;
import com.googlecode.totallylazy.Sequences;
import org.junit.Ignore;
import org.junit.Test;

import static com.googlecode.totallylazy.Segment.constructors.emptySegment;
import static com.googlecode.totallylazy.Segment.constructors.characters;
import static com.googlecode.totallylazy.Unchecked.cast;
import static com.googlecode.totallylazy.matchers.Matchers.is;
import static com.googlecode.totallylazy.parser.CharacterParser.character;
import static com.googlecode.totallylazy.parser.ManyParser.many;
import static org.hamcrest.MatcherAssert.assertThat;

public class ManyParserTest {
    @Test
    @Ignore("WIP")
    public void doesNotThrowIfItConsumesAllCharacters() throws Exception {
        Success<Sequence<Character>> result = cast(many(character('C')).parse(characters("CCCCC")));
        assertThat(result.value(), is(characters("CCCCC")));
        assertThat(result.remainder(), is(emptySegment(Character.class)));
    }

    @Test
    public void supportMany() throws Exception {
        Success<Sequence<Character>> result = cast(many(character('C')).parse(characters("CCCCCDDDD")));
        assertThat(result.value(), is(characters("CCCCC")));
        assertThat(result.remainder(), is(characters("DDDD")));
    }

    @Test
    public void supportChaining() throws Exception {
        Success<Sequence<Character>> result = cast(character('C').many().parse(characters("CCCCCDDDD")));
        assertThat(result.value(), is(characters("CCCCC")));
        assertThat(result.remainder(), is(characters("DDDD")));

    }
}

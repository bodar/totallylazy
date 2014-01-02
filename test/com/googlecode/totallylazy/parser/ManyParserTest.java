package com.googlecode.totallylazy.parser;

import com.googlecode.totallylazy.Sequence;
import com.googlecode.totallylazy.Sequences;
import org.junit.Test;

import static com.googlecode.totallylazy.Segment.constructors.characters;
import static com.googlecode.totallylazy.Segment.constructors.emptySegment;
import static com.googlecode.totallylazy.matchers.Matchers.is;
import static com.googlecode.totallylazy.parser.CharacterParser.character;
import static com.googlecode.totallylazy.parser.ManyParser.many;
import static org.hamcrest.MatcherAssert.assertThat;

public class ManyParserTest {

    @Test
    public void doesNotThrowIfNoCharacters() throws Exception {
        Result<Sequence<Character>> result = many(character('C')).parse("");
        assertThat(result.value(), is(Sequences.characters("")));
        assertThat(result.remainder().toString(), is(""));
    }

    @Test
    public void doesNotThrowIfItConsumesAllCharacters() throws Exception {
        Result<Sequence<Character>> result = many(character('C')).parse("CCCCC");
        assertThat(result.value(), is(Sequences.characters("CCCCC")));
        assertThat(result.remainder().toString(), is(""));
    }

    @Test
    public void supportMany() throws Exception {
        Result<Sequence<Character>> result = many(character('C')).parse("CCCCCDEFG");
        assertThat(result.value(), is(Sequences.characters("CCCCC")));
        assertThat(result.remainder().toString(), is("DEFG"));
    }

    @Test
    public void supportChaining() throws Exception {
        Result<Sequence<Character>> result = character('C').many().parse("CCCCCDEFG");
        assertThat(result.value(), is(Sequences.characters("CCCCC")));
        assertThat(result.remainder().toString(), is("DEFG"));
    }
}

package com.googlecode.totallylazy.parser;

import com.googlecode.totallylazy.Sequence;
import com.googlecode.totallylazy.Sequences;
import org.junit.Test;

import static com.googlecode.totallylazy.Segment.constructors.characters;
import static com.googlecode.totallylazy.Segment.constructors.emptySegment;
import static com.googlecode.totallylazy.Sequences.sequence;
import static com.googlecode.totallylazy.Predicates.is;
import static com.googlecode.totallylazy.parser.CharacterParser.character;
import static com.googlecode.totallylazy.PredicateAssert.assertThat;

public class SequenceParserTest {
    @Test
    public void doesNotThrowIfNoCharacters() throws Exception {
        Result<Sequence<Character>> result = SequenceParser.sequence(character('C')).parse("");
        assertThat(result.value(), is(Sequences.<Character>empty()));
        assertThat(result.remainder(), is(emptySegment(Character.class)));
    }

    @Test
    public void doesNotThrowIfItConsumesAllCharacters() throws Exception {
        Result<Sequence<Character>> result = SequenceParser.sequence(character('C')).parse("CCCCC");
        assertThat(result.value(), is(sequence('C', 'C', 'C', 'C', 'C')));
        assertThat(result.remainder(), is(emptySegment(Character.class)));
    }

    @Test
    public void supportMany() throws Exception {
        Result<Sequence<Character>> result = SequenceParser.sequence(character('C')).parse("CCCCCDEFG");
        assertThat(result.value(), is(sequence('C', 'C', 'C', 'C', 'C')));
        assertThat(result.remainder(), is(characters("DEFG")));
    }

    @Test
    public void supportChaining() throws Exception {
        Result<Sequence<Character>> result = character('C').sequence().parse("CCCCCDEFG");
        assertThat(result.value(), is(sequence('C', 'C', 'C', 'C', 'C')));
        assertThat(result.remainder(), is(characters("DEFG")));
    }
}

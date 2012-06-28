package com.googlecode.totallylazy.parser;

import com.googlecode.totallylazy.Sequence;
import org.junit.Ignore;
import org.junit.Test;

import static com.googlecode.totallylazy.Segment.constructors.characters;
import static com.googlecode.totallylazy.Segment.constructors.emptySegment;
import static com.googlecode.totallylazy.Sequences.sequence;
import static com.googlecode.totallylazy.matchers.Matchers.is;
import static com.googlecode.totallylazy.parser.CharacterParser.character;
import static org.hamcrest.MatcherAssert.assertThat;

public class SequenceParserTest {
    @Test
    public void canCombineTwoParsers() throws Exception {
        Success<Sequence<Character>> result = (Success<Sequence<Character>>) SequenceParser.sequenceOf(character('A'), character('B')).parse(characters("ABC"));
        assertThat(result.value(), is(sequence('A', 'B')));
        assertThat(result.remainder(), is(characters("C")));
    }

    @Test
    @Ignore("WIP")
    public void canCombineThreeParsers() throws Exception {
        Success<Sequence<Character>> result = (Success<Sequence<Character>>) SequenceParser.sequenceOf(character('A'), character('B'), character('C')).
                parse(characters("ABC"));
        assertThat(result.value(), is(sequence('A', 'B', 'C')));
        assertThat(result.remainder(), is(emptySegment(Character.class)));
    }
}

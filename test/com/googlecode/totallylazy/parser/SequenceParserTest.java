package com.googlecode.totallylazy.parser;

import com.googlecode.totallylazy.Pair;
import org.junit.Test;

import static com.googlecode.totallylazy.Pair.pair;
import static com.googlecode.totallylazy.Sequences.characters;
import static com.googlecode.totallylazy.matchers.Matchers.is;
import static com.googlecode.totallylazy.parser.CharacterParser.character;
import static com.googlecode.totallylazy.parser.SequenceParser.sequenceOf;
import static org.hamcrest.MatcherAssert.assertThat;

public class SequenceParserTest {
    @Test
    public void canCombineTwoParsers() throws Exception {
        Success<Pair<Character, Character>> result =
                (Success<Pair<Character, Character>>) sequenceOf(character('A'), character('B')).call(characters("ABC"));
        assertThat(result.value(), is(pair('A', 'B')));
        assertThat(result.remainder(), is(characters("C")));
    }

}

package com.googlecode.totallylazy.parser;

import com.googlecode.totallylazy.Pair;
import org.junit.Test;

import static com.googlecode.totallylazy.Pair.pair;
import static com.googlecode.totallylazy.Assert.assertThat;
import static com.googlecode.totallylazy.Predicates.instanceOf;
import static com.googlecode.totallylazy.Predicates.is;
import static com.googlecode.totallylazy.Segment.constructors.characters;
import static com.googlecode.totallylazy.parser.CharacterParser.character;

public class PairParserTest {
    @Test
    public void canCombineTwoParsers() throws Exception {
        Result<Pair<Character, Character>> result = PairParser.pair(character('A'), character('B')).parse("ABC");
        assertThat(result.value(), is(pair('A', 'B')));
        assertThat(result.remainder(), is(characters("C")));
    }

    @Test
    public void doesBlowUpWhenLessCharacters() throws Exception {
        Result<Pair<Character, Character>> result = PairParser.pair(character('A'), character('B')).parse("A");
        assertThat(result, instanceOf(Failure.class));
    }

}

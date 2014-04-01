package com.googlecode.totallylazy.parser;

import com.googlecode.totallylazy.Triple;
import org.junit.Test;

import static com.googlecode.totallylazy.Segment.constructors.characters;
import static com.googlecode.totallylazy.Triple.triple;
import static com.googlecode.totallylazy.matchers.Matchers.is;
import static com.googlecode.totallylazy.parser.CharacterParser.character;
import static com.googlecode.totallylazy.parser.TripleParser.triple;
import static org.hamcrest.MatcherAssert.assertThat;

public class TripleParserTest {
    @Test
    public void canCombineThreeParsers() throws Exception {
        Result<Triple<Character, Character, Character>> result = triple(character('A'), character('B'), character('C')).parse("ABCD");
        assertThat(result.value(), is(triple('A', 'B', 'C')));
        assertThat(result.remainder(), is(characters("D")));
    }

}

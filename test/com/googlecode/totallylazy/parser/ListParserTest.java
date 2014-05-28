package com.googlecode.totallylazy.parser;

import org.junit.Test;

import java.util.List;

import static com.googlecode.totallylazy.Lists.list;
import static com.googlecode.totallylazy.Segment.constructors.characters;
import static com.googlecode.totallylazy.Segment.constructors.emptySegment;
import static com.googlecode.totallylazy.Predicates.is;
import static com.googlecode.totallylazy.parser.CharacterParser.character;
import static com.googlecode.totallylazy.PredicateAssert.assertThat;

public class ListParserTest {
    @Test
    public void canCombineTwoParsers() throws Exception {
        Parser<List<Character>> parser = Parsers.list(character('A'), character('B'));
        Result<List<Character>> result = parser.parse("ABC");
        assertThat(result.value(), is(list('A', 'B')));
        assertThat(result.remainder(), is(characters("C")));
    }

    @Test
    public void canCombineThreeParsers() throws Exception {
        Result<List<Character>> result = Parsers.list(character('A'), character('B'), character('C')).
                parse("ABC");
        assertThat(result.value(), is(list('A', 'B', 'C')));
        assertThat(result.remainder(), is(emptySegment(Character.class)));
    }
}

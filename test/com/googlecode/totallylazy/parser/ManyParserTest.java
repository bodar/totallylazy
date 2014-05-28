package com.googlecode.totallylazy.parser;

import com.googlecode.totallylazy.Lists;
import org.junit.Test;

import java.util.List;

import static com.googlecode.totallylazy.Lists.list;
import static com.googlecode.totallylazy.Segment.constructors.characters;
import static com.googlecode.totallylazy.Segment.constructors.emptySegment;
import static com.googlecode.totallylazy.Predicates.is;
import static com.googlecode.totallylazy.parser.CharacterParser.character;
import static com.googlecode.totallylazy.parser.ManyParser.many;
import static com.googlecode.totallylazy.Assert.assertThat;

public class ManyParserTest {
    @Test
    public void doesNotThrowIfNoCharacters() throws Exception {
        Result<List<Character>> result = many(character('C')).parse("");
        assertThat(result.value(), is(Lists.<Character>list()));
        assertThat(result.remainder(), is(emptySegment(Character.class)));
    }

    @Test
    public void doesNotThrowIfItConsumesAllCharacters() throws Exception {
        Result<List<Character>> result = many(character('C')).parse("CCCCC");
        assertThat(result.value(), is(list('C', 'C', 'C', 'C', 'C')));
        assertThat(result.remainder(), is(emptySegment(Character.class)));
    }

    @Test
    public void supportMany() throws Exception {
        Result<List<Character>> result = many(character('C')).parse("CCCCCDEFG");
        assertThat(result.value(), is(list('C', 'C', 'C', 'C', 'C')));
        assertThat(result.remainder(), is(characters("DEFG")));
    }

    @Test
    public void supportChaining() throws Exception {
        Result<List<Character>> result = character('C').many().parse("CCCCCDEFG");
        assertThat(result.value(), is(list('C', 'C', 'C', 'C', 'C')));
        assertThat(result.remainder(), is(characters("DEFG")));
    }
}

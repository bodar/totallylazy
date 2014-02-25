package com.googlecode.totallylazy.parser;

import com.googlecode.totallylazy.Lists;
import org.junit.Test;

import java.util.List;

import static com.googlecode.totallylazy.Lists.list;
import static com.googlecode.totallylazy.Segment.constructors.characters;
import static com.googlecode.totallylazy.Segment.constructors.emptySegment;
import static com.googlecode.totallylazy.matchers.Matchers.is;
import static com.googlecode.totallylazy.parser.CharacterParser.character;
import static com.googlecode.totallylazy.parser.ManyParser.many;
import static org.hamcrest.MatcherAssert.assertThat;

public class ManyParserTest {
    @Test
    public void doesNotThrowIfNoCharacters() throws Exception {
        Result<List<Character>> result = many(character('C')).parse(characters(""));
        assertThat(result.value(), is(Lists.<Character>list()));
        assertThat(result.remainder(), is(emptySegment(Character.class)));
    }

    @Test
    public void doesNotThrowIfItConsumesAllCharacters() throws Exception {
        Result<List<Character>> result = many(character('C')).parse(characters("CCCCC"));
        assertThat(result.value(), is(list('C', 'C', 'C', 'C', 'C')));
        assertThat(result.remainder(), is(emptySegment(Character.class)));
    }

    @Test
    public void supportMany() throws Exception {
        Result<List<Character>> result = many(character('C')).parse(characters("CCCCCDEFG"));
        assertThat(result.value(), is(list('C', 'C', 'C', 'C', 'C')));
        assertThat(result.remainder(), is(characters("DEFG")));
    }

    @Test
    public void supportChaining() throws Exception {
        Result<List<Character>> result = character('C').many().parse(characters("CCCCCDEFG"));
        assertThat(result.value(), is(list('C', 'C', 'C', 'C', 'C')));
        assertThat(result.remainder(), is(characters("DEFG")));
    }
}

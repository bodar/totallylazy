package com.googlecode.totallylazy.parser;

import com.googlecode.totallylazy.Sequences;
import org.junit.Test;

import static com.googlecode.totallylazy.Sequences.characters;
import static com.googlecode.totallylazy.Unchecked.cast;
import static com.googlecode.totallylazy.matchers.Matchers.is;
import static com.googlecode.totallylazy.parser.CharacterParser.character;
import static org.hamcrest.MatcherAssert.assertThat;

public class CharacterParserTest {
    @Test
    public void doesNotThrowIfItRunsOutOfCharacters() throws Exception {
        Failure<Character> result = cast(character('A').parse(characters("")));
        assertThat(result.message(), is("A expected"));
    }

    @Test
    public void canParseACharacter() throws Exception {
        Success<Character> result = cast(character('A').parse(characters("ABC")));
        assertThat(result.value(), is('A'));
        assertThat(result.remainder(), is(characters("BC")));
    }

    @Test
    public void handlesNoMatch() throws Exception {
        Failure<Character> result = cast(character('A').parse(characters("CBA")));
        assertThat(result.message(), is("A expected"));
    }
}

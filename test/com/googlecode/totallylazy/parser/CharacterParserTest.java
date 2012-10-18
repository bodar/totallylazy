package com.googlecode.totallylazy.parser;

import org.junit.Test;

import static com.googlecode.totallylazy.Characters.in;
import static com.googlecode.totallylazy.Segment.constructors.characters;
import static com.googlecode.totallylazy.Unchecked.cast;
import static com.googlecode.totallylazy.matchers.Matchers.is;
import static com.googlecode.totallylazy.parser.CharacterParser.character;
import static org.hamcrest.MatcherAssert.assertThat;

public class CharacterParserTest {
    @Test
    public void canCombineWithPredicate() throws Exception {
        Result<Character> result = character(in("AFZ")).parse(characters("ABC"));
        assertThat(result.value(), is('A'));
        assertThat(result.remainder(), is(characters("BC")));
    }

    @Test
    public void doesNotThrowIfItRunsOutOfCharacters() throws Exception {
        Failure<Character> result = cast(character('A').parse(characters("")));
        assertThat(result.message(), is("Expected:'A'"));
    }

    @Test
    public void canParseACharacter() throws Exception {
        Result<Character> result = character('A').parse(characters("ABC"));
        assertThat(result.value(), is('A'));
        assertThat(result.remainder(), is(characters("BC")));
    }

    @Test
    public void handlesNoMatch() throws Exception {
        Failure<Character> result = cast(character('A').parse(characters("CBA")));
        assertThat(result.message(), is("Expected:'A' Actual:'C'"));
    }
}

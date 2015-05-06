package com.googlecode.totallylazy.parser;

import com.googlecode.totallylazy.Characters;
import com.googlecode.totallylazy.Segment;
import org.junit.Test;

import static com.googlecode.totallylazy.Characters.alphaNumeric;
import static com.googlecode.totallylazy.Segment.constructors.emptySegment;
import static com.googlecode.totallylazy.matchers.Matchers.is;
import static com.googlecode.totallylazy.parser.CharactersParser.characters;
import static org.hamcrest.MatcherAssert.assertThat;


public class CharactersParserTest {
    @Test
    public void parsesMultipleCharacters() throws Exception {
        Result<CharSequence> result = characters(alphaNumeric).parse("ABC");
        assertThat(result.value(), is("ABC"));
        assertThat(result.remainder(), is(emptySegment(Character.class)));
    }

    @Test
    public void leavesRemainder() throws Exception {
        Result<CharSequence> result = characters(alphaNumeric).parse("ABC+");
        assertThat(result.value(), is("ABC"));
        assertThat(result.remainder(), is(Segment.constructors.characters("+")));
    }

    @Test
    public void matchIsRequired() throws Exception {
        assertThat(characters(alphaNumeric).parse("+").failure(), is(true));
        assertThat(characters(alphaNumeric).parse("").failure(), is(true));
    }
}
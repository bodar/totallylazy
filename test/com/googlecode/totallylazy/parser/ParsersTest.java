package com.googlecode.totallylazy.parser;

import com.googlecode.totallylazy.Segment;
import org.junit.Test;

import static com.googlecode.totallylazy.Segment.constructors.characters;
import static com.googlecode.totallylazy.matchers.Matchers.is;
import static com.googlecode.totallylazy.parser.CharacterParser.character;
import static org.hamcrest.MatcherAssert.assertThat;

public class ParsersTest {
    @Test
    public void supportsNext() throws Exception {
        Success<Character> result = (Success<Character>) character('A').next(character('B')).parse(characters("ABC"));
        assertThat(result.value(), is('B'));
        assertThat(result.remainder().head(), is('C'));
    }

    @Test
    public void supportsFollowedBy() throws Exception {
        Success<Character> result = (Success<Character>) character('A').followedBy(character('B')).parse(characters("ABC"));
        assertThat(result.value(), is('A'));
        assertThat(result.remainder().head(), is('C'));
    }

    @Test
    public void supportsBetween() throws Exception {
        Success<Character> result = (Success<Character>) character('B').between(character('A'), character('C')).parse(characters("ABCD"));
        assertThat(result.value(), is('B'));
        assertThat(result.remainder().head(), is('D'));
    }

    @Test
    public void supportsSurroundedBy() throws Exception {
        Success<Character> result = (Success<Character>) character('B').surroundedBy(character('$')).parse(characters("$B$D"));
        assertThat(result.value(), is('B'));
        assertThat(result.remainder().head(), is('D'));
    }

    @Test
    public void supportsSeparatedBy() throws Exception {
        Success<Segment<Character>> result = (Success<Segment<Character>>) character('A').separatedBy(character(',')).parse(characters("A,A,ABC"));
        assertThat(result.value(), is(characters("AAA")));
        assertThat(result.remainder().head(), is('B'));
    }
}

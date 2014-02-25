package com.googlecode.totallylazy.parser;

import com.googlecode.totallylazy.Segment;
import org.junit.Test;

import java.util.List;

import static com.googlecode.totallylazy.Lists.list;
import static com.googlecode.totallylazy.Segment.constructors.characters;
import static com.googlecode.totallylazy.matchers.Matchers.is;
import static com.googlecode.totallylazy.parser.CharacterParser.character;
import static org.hamcrest.MatcherAssert.assertThat;

public class ParsersTest {
    @Test
    public void supportsNext() throws Exception {
        Result<Character> result = character('A').next(character('B')).parse(characters("ABC"));
        assertThat(result.value(), is('B'));
        assertThat(result.remainder().head(), is('C'));
    }

    @Test
    public void supportsFollowedBy() throws Exception {
        Result<Character> result = character('A').followedBy(character('B')).parse(characters("ABC"));
        assertThat(result.value(), is('A'));
        assertThat(result.remainder().head(), is('C'));
    }

    @Test
    public void supportsBetween() throws Exception {
        Result<Character> result = character('B').between(character('A'), character('C')).parse(characters("ABCD"));
        assertThat(result.value(), is('B'));
        assertThat(result.remainder().head(), is('D'));
    }

    @Test
    public void supportsSurroundedBy() throws Exception {
        Result<Character> result = character('B').surroundedBy(character('$')).parse(characters("$B$D"));
        assertThat(result.value(), is('B'));
        assertThat(result.remainder().head(), is('D'));
    }

    @Test
    public void supportsSeparatedBy() throws Exception {
        Result<List<Character>> result = character('A').separatedBy(character(',')).parse(characters("A,A,ABC"));
        assertThat(result.value(), is(list('A','A','A')));
        assertThat(result.remainder().head(), is('B'));
    }

//    @Test
//    public void supportsIdentifier() throws Exception {
//        Result<String> result = identifier.parse(characters("sayHello()"));
//        assertThat(result.value(), is("sayHello"));
//        assertThat(result.remainder(), is(characters("()")));
//    }
}

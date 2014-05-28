package com.googlecode.totallylazy.parser;

import org.junit.Test;

import java.util.List;

import static com.googlecode.totallylazy.Lists.list;
import static com.googlecode.totallylazy.Assert.assertThat;
import static com.googlecode.totallylazy.Predicates.is;
import static com.googlecode.totallylazy.parser.CharacterParser.character;

public class ParsersTest {
    @Test
    public void supportsNext() throws Exception {
        Result<Character> result = character('A').next(character('B')).parse("ABC");
        assertThat(result.value(), is('B'));
        assertThat(result.remainder().head(), is('C'));
    }

    @Test
    public void supportsFollowedBy() throws Exception {
        Result<Character> result = character('A').followedBy(character('B')).parse("ABC");
        assertThat(result.value(), is('A'));
        assertThat(result.remainder().head(), is('C'));
    }

    @Test
    public void supportsBetween() throws Exception {
        Result<Character> result = character('B').between(character('A'), character('C')).parse("ABCD");
        assertThat(result.value(), is('B'));
        assertThat(result.remainder().head(), is('D'));
    }

    @Test
    public void supportsSurroundedBy() throws Exception {
        Result<Character> result = character('B').surroundedBy(character('$')).parse("$B$D");
        assertThat(result.value(), is('B'));
        assertThat(result.remainder().head(), is('D'));
    }

    @Test
    public void supportsSeparatedBy() throws Exception {
        Result<List<Character>> result = character('A').separatedBy(character(',')).parse("A,A,ABC");
        assertThat(result.value(), is(list('A', 'A', 'A')));
        assertThat(result.remainder().head(), is('B'));
    }

//    @Test
//    public void supportsIdentifier() throws Exception {
//        Result<String> result = identifier.parse(characters("sayHello()"));
//        assertThat(result.value(), is("sayHello"));
//        assertThat(result.remainder(), is(characters("()")));
//    }
}

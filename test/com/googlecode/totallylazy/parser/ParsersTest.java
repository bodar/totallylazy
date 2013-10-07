package com.googlecode.totallylazy.parser;

import com.googlecode.totallylazy.Sequence;
import com.googlecode.totallylazy.Sequences;
import org.junit.Test;

import static com.googlecode.totallylazy.Segment.constructors.characters;
import static com.googlecode.totallylazy.Sequences.sequence;
import static com.googlecode.totallylazy.matchers.Matchers.is;
import static com.googlecode.totallylazy.parser.CharacterParser.character;
import static com.googlecode.totallylazy.parser.Parsers.identifier;
import static org.hamcrest.MatcherAssert.assertThat;

public class ParsersTest {
    @Test
    public void supportsNext() throws Exception {
        Result<Character> result = character('A').next(character('B')).parse("ABC");
        assertThat(result.value(), is('B'));
        assertThat(result.remainder().get(), is('C'));
    }

    @Test
    public void supportsFollowedBy() throws Exception {
        Result<Character> result = character('A').followedBy(character('B')).parse("ABC");
        assertThat(result.value(), is('A'));
        assertThat(result.remainder().get(), is('C'));
    }

    @Test
    public void supportsBetween() throws Exception {
        Result<Character> result = character('B').between(character('A'), character('C')).parse("ABCD");
        assertThat(result.value(), is('B'));
        assertThat(result.remainder().get(), is('D'));
    }

    @Test
    public void supportsSurroundedBy() throws Exception {
        Result<Character> result = character('B').surroundedBy(character('$')).parse("$B$D");
        assertThat(result.value(), is('B'));
        assertThat(result.remainder().get(), is('D'));
    }

    @Test
    public void supportsSeparatedBy() throws Exception {
        Result<Sequence<Character>> result = character('A').separatedBy(character(',')).parse("A,A,ABC");
        Sequence<Character> value = result.value();
        assertThat(value, is(Sequences.characters("AAA")));
        assertThat(result.remainder().get(), is('B'));
    }

    @Test
    public void supportsIdentifier() throws Exception {
        Result<String> result = identifier.parse("sayHello()");
        assertThat(result.value(), is("sayHello"));
        assertThat(result.remainder().toString(), is("()"));
    }
}

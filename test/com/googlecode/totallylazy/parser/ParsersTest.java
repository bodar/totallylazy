package com.googlecode.totallylazy.parser;

import org.junit.Test;

import java.util.List;

import static com.googlecode.totallylazy.Assert.assertThat;
import static com.googlecode.totallylazy.Lists.list;
import static com.googlecode.totallylazy.Predicates.is;
import static com.googlecode.totallylazy.Segment.constructors.characters;
import static com.googlecode.totallylazy.parser.CharacterParser.character;
import static com.googlecode.totallylazy.parser.Parsers.pattern;
import static com.googlecode.totallylazy.parser.Parsers.string;
import static java.lang.String.format;

public class ParsersTest {
    @Test
    public void supportsLeftAssociativeInfix() throws Exception {
        Parser<String> parser = pattern("[A-C]").infixLeft(string(" AND ").map(ignore -> (a, b) -> format("( %s + %s )", a, b)));
        String result = parser.parse(characters("A AND B AND C")).value();
        assertThat(result, is("( ( A + B ) + C )"));
    }

    @Test
    public void supportsRightAssociativeInfix() throws Exception {
        Parser<String> parser = pattern("[A-C]").infixRight(string(" AND ").map(ignore -> (a, b) -> format("( %s + %s )", a, b)));
        String result = parser.parse(characters("A AND B AND C")).value();
        assertThat(result, is("( A + ( B + C ) )"));
    }

    @Test
    public void supportsPeek() throws Exception {
        Parser<Character> parser = character('A').peek(character('B'));
        Result<Character> success = parser.parse("ABC");
        assertThat(success.value(), is('A'));
        assertThat(success.remainder(), is(characters("BC")));
        Result<Character> failure = parser.parse("AC");
        assertThat(failure.message(), is("B expected, C encountered."));
    }

    @Test
    public void supportsReturns() throws Exception {
        Result<Integer> result = character('A').returns(1).parse("ABC");
        assertThat(result.value(), is(1));
        assertThat(result.remainder().head(), is('B'));
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
}

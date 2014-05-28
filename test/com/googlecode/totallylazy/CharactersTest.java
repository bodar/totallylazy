package com.googlecode.totallylazy;

import org.junit.Ignore;
import org.junit.Test;

import static com.googlecode.totallylazy.Characters.ASCII;
import static com.googlecode.totallylazy.Characters.UTF16;
import static com.googlecode.totallylazy.Characters.UTF8;
import static com.googlecode.totallylazy.Characters.characters;
import static com.googlecode.totallylazy.Characters.in;
import static com.googlecode.totallylazy.Characters.range;
import static com.googlecode.totallylazy.Characters.set;
import static com.googlecode.totallylazy.Files.file;
import static com.googlecode.totallylazy.Files.workingDirectory;
import static com.googlecode.totallylazy.Predicates.is;
import static java.lang.Character.MAX_VALUE;
import static java.lang.Character.MIN_VALUE;
import static com.googlecode.totallylazy.PredicateAssert.assertThat;

public class CharactersTest {
    @Test
    public void canDetectedIfACharsetContainsACharacter() throws Exception {
        assertThat(characters("λΣ").forAll(in(UTF8)), is(true));
        assertThat(characters("λΣ").forAll(in(ASCII)), is(false));
    }

    @Test
    public void canCreateARangeOfCharacters() throws Exception {
        Sequence<Character> range = range(MIN_VALUE, MAX_VALUE);
        assertThat(range.size(), is(65536));
        assertThat(range.contains('λ'), is(true));
    }

    @Test
    public void canConvertACharsetIntoARealSet() throws Exception {
        assertThat(set(UTF8).contains('λ'), is(true));
    }

    @Test
    @Ignore
    public void canDetectValidJavaIdentifier() throws Exception {
        String chars = characters(UTF16).filter(Characters.identifierStart).toString(" ");
        Files.write(chars.getBytes(UTF16), file(workingDirectory(), "javaidentifiers.txt"));
    }
}

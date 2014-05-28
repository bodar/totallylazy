package com.googlecode.totallylazy.segments;

import com.googlecode.totallylazy.Segment;
import org.junit.Test;

import java.util.NoSuchElementException;

import static com.googlecode.totallylazy.Option.none;
import static com.googlecode.totallylazy.Option.some;
import static com.googlecode.totallylazy.Assert.assertThat;
import static com.googlecode.totallylazy.Predicates.is;
import static com.googlecode.totallylazy.segments.CharacterSegment.characterSegment;
import static com.googlecode.totallylazy.Assert.fail;

public class CharacterSegmentTest {
    @Test
    public void head() throws Exception {
        Segment<Character> segment = characterSegment("Hello");
        assertThat(segment.head(), is('H'));
        assertThat(segment.tail().head(), is('e'));
        assertThat(segment.tail().tail().head(), is('l'));
        assertThat(segment.tail().tail().tail().head(), is('l'));
        assertThat(segment.tail().tail().tail().tail().head(), is('o'));
    }

    @Test
    public void isEmpty() throws Exception {
        Segment<Character> segment = characterSegment("Hello");
        assertThat(segment.isEmpty(), is(false));
        assertThat(segment.tail().isEmpty(), is(false));
        assertThat(segment.tail().tail().isEmpty(), is(false));
        assertThat(segment.tail().tail().tail().isEmpty(), is(false));
        assertThat(segment.tail().tail().tail().tail().isEmpty(), is(false));
        assertThat(segment.tail().tail().tail().tail().tail().isEmpty(), is(true));
    }

    @Test
    public void tailShouldThrowNoSuchElementException() throws Exception {
        try {
            characterSegment("").tail();
            fail("Should have thrown NoSuchElementException");
        } catch (NoSuchElementException e) {
        }
    }

    @Test
    public void headShouldThrowNoSuchElementException() throws Exception {
        try {
            characterSegment("").head();
            fail("Should have thrown NoSuchElementException");
        } catch (NoSuchElementException e) {
        }
    }

    @Test
    public void supportsHeadOption() {
        assertThat(characterSegment("").headOption(), is(none(Character.class)));
        assertThat(characterSegment("ABC").headOption(), is(some('A')));
    }


    @Test
    public void supportsEquality() throws Exception {
        assertThat(characterSegment("").equals(characterSegment("")), is(true));
        assertThat(characterSegment("").equals(characterSegment("d")), is(false));
        assertThat(characterSegment("d").equals(characterSegment("d")), is(true));
        assertThat(characterSegment("d").equals(characterSegment("db")), is(false));
        assertThat(characterSegment("db").equals(characterSegment("db")), is(true));
    }
}
package com.googlecode.totallylazy.segments;

import com.googlecode.totallylazy.Segment;
import org.junit.Test;

import static com.googlecode.totallylazy.matchers.Matchers.is;
import static com.googlecode.totallylazy.segments.CharacterSegment.characterSegment;
import static org.hamcrest.MatcherAssert.assertThat;

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
    public void ifOffsetIsGreaterOrEqualThanLengthReturnEmpty() throws Exception {
        assertThat(characterSegment("", 0).isEmpty(), is(true));
        assertThat(characterSegment("", 1).isEmpty(), is(true));
        assertThat(characterSegment("a", 1).isEmpty(), is(true));
        assertThat(characterSegment("a", 2).isEmpty(), is(true));
        assertThat(characterSegment("a", 0).isEmpty(), is(false));
    }
}
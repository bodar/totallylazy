package com.googlecode.totallylazy.parser;

import com.googlecode.totallylazy.Segment;
import org.junit.Test;

import static com.googlecode.totallylazy.Predicates.is;
import static com.googlecode.totallylazy.PredicateAssert.assertThat;

public class CharacterSequenceTest {
    @Test
    public void charAt() throws Exception {
        CharacterSequence word = CharacterSequence.charSequence(Segment.constructors.characters("Hello"));
        assertThat(word.charAt(0), is('H'));
        assertThat(word.charAt(1), is('e'));
        assertThat(word.charAt(2), is('l'));
        assertThat(word.charAt(3), is('l'));
        assertThat(word.charAt(4), is('o'));
    }

    @Test
    public void subSequence() throws Exception {
        CharacterSequence word = CharacterSequence.charSequence(Segment.constructors.characters("Hello"));
        assertThat(word.subSequence(0, 1), is((CharSequence)"H"));
        assertThat(word.subSequence(0, 2), is((CharSequence)"He"));
        assertThat(word.subSequence(0, 3), is((CharSequence)"Hel"));
        assertThat(word.subSequence(0, 4), is((CharSequence)"Hell"));
        assertThat(word.subSequence(0, 5), is((CharSequence)"Hello"));
    }
}

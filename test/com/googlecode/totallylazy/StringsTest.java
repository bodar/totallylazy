package com.googlecode.totallylazy;

import org.junit.Test;

import static com.googlecode.totallylazy.Sequences.repeat;
import static com.googlecode.totallylazy.Sequences.sequence;
import static com.googlecode.totallylazy.Strings.toLowerCase;
import static com.googlecode.totallylazy.Strings.toUpperCase;
import static com.googlecode.totallylazy.matchers.IterableMatcher.hasExactly;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class StringsTest {
    @Test
    public void canMapToStringFunctions() throws Exception {
       assertThat(sequence("Dan").map(toLowerCase()), hasExactly("dan"));
       assertThat(sequence("Dan").map(toUpperCase()), hasExactly("DAN"));
    }

    @Test
    public void canEscapeXml() throws Exception {
        assertThat(Strings.escapeXml("& < > ' " + new Character((char)0x80)), is("&amp; &lt; &gt; &apos; &#128;"));
    }

     @Test
    public void doesNotTruncateString() throws Exception {
        String testString = longStringWithoutEncodedChars();
         assertThat(Strings.escapeXml(testString), is(testString));
    }

    private String longStringWithoutEncodedChars() {
        return repeat("A").take(100).toString("", "", "", Long.MAX_VALUE);
    }
}

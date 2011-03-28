package com.googlecode.totallylazy;

import org.junit.Test;

import java.io.File;

import static com.googlecode.totallylazy.Files.temporaryFile;
import static com.googlecode.totallylazy.Files.write;
import static com.googlecode.totallylazy.Sequences.repeat;
import static com.googlecode.totallylazy.Sequences.sequence;
import static com.googlecode.totallylazy.Strings.lines;
import static com.googlecode.totallylazy.Strings.toLowerCase;
import static com.googlecode.totallylazy.Strings.toUpperCase;
import static com.googlecode.totallylazy.matchers.IterableMatcher.hasExactly;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class StringsTest {
    @Test
    public void parsesLines() throws Exception {
        File file = temporaryFile();
        write("1\r\n2".getBytes("UTF-8"), file);
        Sequence<String> lines = lines(file);
        assertThat(lines, hasExactly("1", "2"));
    }
    @Test
    public void canMapToStringFunctions() throws Exception {
        assertThat(sequence("Dan").map(toLowerCase()), hasExactly("dan"));
        assertThat(sequence("Dan").map(toUpperCase()), hasExactly("DAN"));
    }

    @Test
    public void canEscapeXml() throws Exception {
        assertThat(Strings.escapeXml("& < > ' " + new Character((char) 0x80)), is("&amp; &lt; &gt; &apos; &#128;"));
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

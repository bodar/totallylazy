package com.googlecode.totallylazy;

import org.junit.Test;

import java.io.File;

import static com.googlecode.totallylazy.Files.temporaryFile;
import static com.googlecode.totallylazy.Files.write;
import static com.googlecode.totallylazy.Sequences.sequence;
import static com.googlecode.totallylazy.Strings.lines;
import static com.googlecode.totallylazy.Strings.reverse;
import static com.googlecode.totallylazy.Strings.substring;
import static com.googlecode.totallylazy.Strings.toLowerCase;
import static com.googlecode.totallylazy.Strings.toUpperCase;
import static com.googlecode.totallylazy.matchers.IterableMatcher.hasExactly;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.fail;

public class StringsTest {
    @Test
    public void supportsToString() throws Exception {
        File file = temporaryFile();
        String input = "1\r\n2";
        write(input.getBytes("UTF-8"), file);
        String output = Strings.toString(file);
        assertThat(output, is(input));
    }

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
    public void supportsSubstring() throws Exception {
        assertThat(sequence("abcdXYZ").map(substring(1, 4)), hasExactly("bcd"));

        assertThat(Strings.substring("abcdXYZ", -3, -1), is("XY"));
        assertThat(Strings.substring("abcdXYZ", -1, -3), is("YX"));

        assertThat(Strings.substring("abcdXYZ", 1, 4), is("bcd"));
        assertThat(Strings.substring("abcdXYZ", 4, 1), is("dcb"));

        assertThat(Strings.substring("abcdXYZ", -4, 1), is("cb"));
        assertThat(Strings.substring("abcdXYZ", 1, -4), is("bc"));

        assertThat(Strings.substring("abcdXYZ", 0, -3), is("abcd"));
        assertThat(Strings.substring("abcdXYZ", 0, 4), is("abcd"));

        assertThat(Strings.substring("abcdXYZ", 4, 0), is("dcba"));
        assertThat(Strings.substring("abcdXYZ", -4, 0), is("cba"));

        try {
            Strings.substring("abc", -4, -1);
            fail();
        } catch(IndexOutOfBoundsException e) {}
    }

    @Test
    public void supportsReverse() throws Exception {
        assertThat(sequence("abc").map(reverse()), hasExactly("cba"));
    }
}

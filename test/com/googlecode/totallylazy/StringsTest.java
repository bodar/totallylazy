package com.googlecode.totallylazy;

import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.StringReader;

import static com.googlecode.totallylazy.Files.temporaryFile;
import static com.googlecode.totallylazy.Files.write;
import static com.googlecode.totallylazy.Predicates.nullValue;
import static com.googlecode.totallylazy.Sequences.empty;
import static com.googlecode.totallylazy.Sequences.sequence;
import static com.googlecode.totallylazy.Strings.bytes;
import static com.googlecode.totallylazy.Strings.join;
import static com.googlecode.totallylazy.Strings.lines;
import static com.googlecode.totallylazy.Strings.maximum;
import static com.googlecode.totallylazy.Strings.minimum;
import static com.googlecode.totallylazy.Strings.replace;
import static com.googlecode.totallylazy.Strings.replaceAll;
import static com.googlecode.totallylazy.Strings.replaceFirst;
import static com.googlecode.totallylazy.Strings.reverse;
import static com.googlecode.totallylazy.Strings.substring;
import static com.googlecode.totallylazy.Strings.toLowerCase;
import static com.googlecode.totallylazy.Strings.toUpperCase;
import static com.googlecode.totallylazy.Uri.uri;
import static com.googlecode.totallylazy.matchers.IterablePredicates.hasExactly;
import static com.googlecode.totallylazy.PredicateAssert.assertThat;
import static com.googlecode.totallylazy.Predicates.is;
import static com.googlecode.totallylazy.PredicateAssert.fail;

public class StringsTest {
    @Test
    public void canCoerceToString() throws Exception {
        assertThat(Strings.string((Object)null), is(""));
        assertThat(Strings.string("foo".getBytes()), is("foo"));
        assertThat(Strings.string(new ByteArrayInputStream("foo".getBytes())), is("foo"));
        assertThat(Strings.string(new StringReader("foo")), is("foo"));
        File file = Files.write("foo".getBytes(), temporaryFile());
        assertThat(Strings.string(file), is("foo"));
    }

        @Test
    public void canJoinStrings() throws Exception {
        assertThat(sequence("foo", " bar ", "baz").reduce(join), is("foo bar baz"));
        assertThat(empty(String.class).reduce(join), is(""));
    }

    @Test
    public void supportsToString() throws Exception {
        File file = temporaryFile();
        String input = "1\r\n2";
        write(bytes(input), file);
        String output = Strings.toString(file);
        assertThat(output, is(input));
    }

    @Test
    public void supportsObjectAsString() throws Exception {
        assertThat(Strings.asString(null), is(""));
        assertThat(Strings.asString(("foo")), is("foo"));
    }

    @Test
    public void parsesLines() throws Exception {
        File file = temporaryFile();
        write(bytes("1\r\n2"), file);
        Sequence<String> lines = lines(file);
        assertThat(lines, hasExactly("1", "2"));
    }

    @Test
    public void canMapToStringFunctions() throws Exception {
        assertThat(sequence("Dan").map(toLowerCase()), hasExactly("dan"));
        assertThat(sequence("Dan").map(toUpperCase()), hasExactly("DAN"));
    }

    @Test
    public void canReplaceCharacters() throws Exception {
        assertThat(sequence("A_TEST").map(replace('_', ' ')), hasExactly("A TEST"));
        assertThat(sequence("A_TEST").map(replace("_", " ")), hasExactly("A TEST"));
        assertThat(sequence("A TEST EXAMPLE").map(replaceAll("\\s", "_")), hasExactly("A_TEST_EXAMPLE"));
        assertThat(sequence("A TEST EXAMPLE").map(replaceFirst("\\s", "_")), hasExactly("A_TEST EXAMPLE"));
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
        } catch (IndexOutOfBoundsException e) {
        }
    }

    @Test
    public void supportsReverse() throws Exception {
        assertThat(sequence("abc").map(reverse()), hasExactly("cba"));
    }

    @Test
    public void supportsIsBlank() throws Exception {
        assertThat(Strings.isBlank(null), is(true));
        assertThat(Strings.isBlank(""), is(true));
        assertThat(Strings.isBlank(" "), is(true));
        assertThat(Strings.isBlank("\t"), is(true));
        assertThat(Strings.isBlank("\n"), is(true));
        assertThat(Strings.isBlank("\r\n"), is(true));

        assertThat(Strings.isBlank(" din"), is(false));
        assertThat(Strings.isBlank("\tdin"), is(false));
        assertThat(Strings.isBlank("\rdin"), is(false));
    }

    @Test
    public void supportsMinimum() throws Exception {
        assertThat(sequence("b", "a", null).reduce(minimum), is("a"));
        assertThat(sequence(null, "a", null).reduce(minimum), is("a"));
        assertThat(sequence("b", "a", "c").reduce(minimum), is("a"));
        assertThat(sequence("b", "a").reduce(minimum), is("a"));
        assertThat(sequence("a").reduce(minimum), is("a"));
        assertThat(Sequences.<String>sequence().reduce(minimum), nullValue());
    }

    @Test
    public void supportsMaximum() throws Exception {
        assertThat(sequence("b", "a", null).reduce(maximum), is("b"));
        assertThat(sequence(null, "a", null).reduce(maximum), is("a"));
        assertThat(sequence("b", "a", "c").reduce(maximum), is("c"));
        assertThat(sequence("b", "a").reduce(maximum), is("b"));
        assertThat(sequence("a").reduce(maximum), is("a"));
        assertThat(Sequences.<String>sequence().reduce(maximum), nullValue());

    }

    @Test
    public void supportsSplit() throws Exception {
        assertThat(sequence("a,b,c").flatMap(Strings.split(",")), hasExactly("a","b","c"));
        assertThat(Strings.split(",").call("a"), hasExactly("a"));
        assertThat(Strings.split(",").call(""), hasExactly(""));
    }
}

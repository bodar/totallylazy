package com.googlecode.totallylazy;

import com.googlecode.totallylazy.matchers.NumberMatcher;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static com.googlecode.totallylazy.Assert.assertThat;
import static com.googlecode.totallylazy.Predicates.is;
import static com.googlecode.totallylazy.Predicates.nullValue;

public class UrlEncodedMessageTest {
    @Test
    public void canParseToPairs() throws Exception {
        List<Pair<String, String>> pairs = UrlEncodedMessage.parse("The+string=%C3%BC%40foo-bar");
        assertThat(pairs.size(), NumberMatcher.is(1));
        assertThat(pairs.get(0).first(), is("The string"));
        if (!runningOnAMac()) {
            assertThat(pairs.get(0).second(), is("ü@foo-bar"));
        }
    }

    @Test
    public void canParseToPairsEvenWhenNoValueIsPresent() throws Exception {
        List<Pair<String, String>> pairs = UrlEncodedMessage.parse("The+string=");
        assertThat(pairs.size(), NumberMatcher.is(1));
        assertThat(pairs.get(0).first(), is("The string"));
        assertThat(pairs.get(0).second(), is(""));
    }

    @Test
    public void preservesTheDifferenceBetweenNullAndEmptyString() throws Exception {
        String original = "one&two=";
        List<Pair<String, String>> pairs = UrlEncodedMessage.parse(original);
        assertThat(pairs.size(), NumberMatcher.is(2));
        assertThat(pairs.get(0).first(), is("one"));
        assertThat(pairs.get(0).second(), nullValue());
        assertThat(pairs.get(1).first(), is("two"));
        assertThat(pairs.get(1).second(), is(""));
        assertThat(UrlEncodedMessage.toString(pairs), is(original));
    }

    @Test
    public void canConvertToString() throws Exception {
        if (runningOnAMac()) {
            return;
        }

        List<Pair<String, String>> pairs = new ArrayList<Pair<String, String>>() {{
            add(Pair.pair("The string", "ü@foo-bar"));
        }};
        String result = UrlEncodedMessage.toString(pairs);
        assertThat(result, is("The+string=%C3%BC%40foo-bar"));
    }

    private static boolean runningOnAMac() {
        return System.getProperty("os.name").contains("Mac OS");
    }
}
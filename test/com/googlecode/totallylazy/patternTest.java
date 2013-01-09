package com.googlecode.totallylazy;

import org.junit.Test;

import static com.googlecode.totallylazy.matchers.Matchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class patternTest {
    @Test
    public void worksWithInstances() throws Exception {
        assertThat(new pattern((Object) "A String") {
            String match(String s) { return "String processed"; }
            String match(CharSequence s) { return "CharSequence processed"; }
            String match(Integer s) { return "Integer processed"; }
        }.<String>match(), is("String processed"));
    }

    @Test
    public void supportsNoMatch() throws Exception {
        assertThat(new pattern((Object) "A String") {
            String match(Integer s) { return "Integer processed"; }
        }.<String>matchOption().getOrElse("No match found"), is("No match found"));
    }
}

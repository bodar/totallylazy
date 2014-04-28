package com.googlecode.totallylazy;

import org.junit.Test;

import static com.googlecode.totallylazy.matchers.Matchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class RecordTest {
    @Test
    public void supportEquality() throws Exception {
        assertThat(new Record(){ int x = 1; int y = 2; }.equals(new Record(){ int x = 1; int y = 2; }), is(true));
        assertThat(new Record(){ int x = 1; int y = 2; }.equals(new Record(){ int x = 1; int y = 5; }), is(false));
        assertThat(new Record(){ int x = 1; int y = 2; }.equals(new Record() {
            int x = 1;
            int z = 2;
        }), is(false));
    }

    @Test
    public void supportsToString() throws Exception {
        assertThat(new Record(){ int x = 1; String y = "2"; }.toString(), is("{x=1, y=2}"));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void isImmutable() throws Exception {
        new Record(){ int x = 1; int y = 2; }.remove("x");
    }
}

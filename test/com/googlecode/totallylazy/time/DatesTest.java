package com.googlecode.totallylazy.time;

import org.junit.Test;

import java.util.Date;

import static com.googlecode.totallylazy.time.Dates.addSeconds;
import static com.googlecode.totallylazy.time.Dates.date;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class DatesTest {
    @Test
    public void canParseAtomDate() throws Exception {
        Date result = Dates.RFC3339().parse("2011-07-19T12:43:26Z");
        assertThat(result, is(date(2011, 7, 19, 12, 43, 26)));
    }

    @Test
    public void canParseRss2Date() throws Exception {
        Date result = Dates.RFC822().parse("Tue, 02 Aug 2011 09:22:53 GMT");
        assertThat(result, is(date(2011, 8, 2, 9, 22, 53)));
    }

    @Test
    public void canAddToDates() throws Exception {
        assertThat(addSeconds(date(2000, 1, 1), 60), is(date(2000, 1, 1, 0, 0, 60)));
    }
}
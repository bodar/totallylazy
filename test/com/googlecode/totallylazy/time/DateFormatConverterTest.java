package com.googlecode.totallylazy.time;

import org.hamcrest.Matchers;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.fail;

public class DateFormatConverterTest {
    @Test
    public void triesMultipleFormatsBeforeGivingUp() throws Exception {
        DateFormatConverter converter = new DateFormatConverter(Dates.RFC3339(), Dates.RFC822(), Dates.javaUtilDateToString());
        assertThat(converter.toDate("2011-07-19T12:43:26Z"), Matchers.is(Dates.date(2011, 7, 19, 12, 43, 26)));
        assertThat(converter.toDate("Thu, 08 Sep 2011 07:14:14 GMT"), Matchers.is(Dates.date(2011, 9, 8, 7, 14, 14)));
        assertThat(converter.toDate("Thu Sep 15 13:27:31 GMT 2011"), Matchers.is(Dates.date(2011, 9, 15, 13, 27, 31)));
        try {
            converter.toDate("gibberish");
            fail("should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // all good
        }
    }

    @Test
    public void usesFirst() throws Exception {
        DateFormatConverter converter = new DateFormatConverter(Dates.RFC3339(), Dates.RFC822());
        assertThat(converter.toString(Dates.date(2011, 9, 8, 7, 14, 14)), is("2011-09-08T07:14:14Z"));
    }
}

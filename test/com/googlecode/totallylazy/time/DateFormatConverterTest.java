package com.googlecode.totallylazy.time;

import org.junit.Test;

import static com.googlecode.totallylazy.Assert.assertThat;
import static com.googlecode.totallylazy.Predicates.is;
import static com.googlecode.totallylazy.Sequences.sequence;
import static com.googlecode.totallylazy.time.Dates.date;
import static com.googlecode.totallylazy.Assert.fail;

public class DateFormatConverterTest {

    @Test
    public void canParseSpecificDate() throws Exception{
        DateFormatConverter converter = new DateFormatConverter(Dates.RFC3339().formats().join(sequence(Dates.RFC822(), Dates.javaUtilDateToString())));
        assertThat(converter.parse("1970-01-06T18:53:20Z"), is(date(1970, 1, 6, 18, 53, 20)));
        assertThat(converter.parse("1970-01-06T18:53:20.123Z"), is(date(1970, 1, 6, 18, 53, 20, 123)));
    }

    @Test
    public void triesMultipleFormatsBeforeGivingUp() throws Exception {
        DateFormatConverter converter = new DateFormatConverter(Dates.RFC3339().formats().join(sequence(Dates.RFC822(), Dates.javaUtilDateToString())));
        assertThat(converter.parse("2011-07-19T12:43:26Z"), is(date(2011, 7, 19, 12, 43, 26)));
        assertThat(converter.parse("Thu, 08 Sep 2011 07:14:14 GMT"), is(date(2011, 9, 8, 7, 14, 14)));
        assertThat(converter.parse("Thu Sep 15 13:27:31 GMT 2011"), is(date(2011, 9, 15, 13, 27, 31)));
        try {
            converter.parse("gibberish");
            fail("should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // all good
        }
    }

    @Test
    public void usesFirst() throws Exception {
        DateFormatConverter converter = new DateFormatConverter(Dates.RFC3339().formats().join(sequence(Dates.RFC822())));
        assertThat(converter.format(date(2011, 9, 8, 7, 14, 14, 123)), is("2011-09-08T07:14:14.123Z"));
    }
}

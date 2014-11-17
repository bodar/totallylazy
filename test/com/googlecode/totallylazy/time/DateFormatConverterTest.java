package com.googlecode.totallylazy.time;

import org.hamcrest.Matchers;
import org.junit.Test;

import static com.googlecode.totallylazy.Sequences.sequence;
import static com.googlecode.totallylazy.time.Dates.date;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.fail;

public class DateFormatConverterTest {

    @Test
    public void supportsRFC3339DateOnly() throws Exception{
        DateFormatConverter converter = new DateFormatConverter(Dates.RFC3339().formats());
        assertThat(converter.parse("1970-01-06"), Matchers.is(date(1970, 1, 6)));
    }

    @Test
    public void canParseSpecificDate() throws Exception{
        DateFormatConverter converter = new DateFormatConverter(Dates.RFC3339().formats().join(sequence(Dates.RFC822(), Dates.javaUtilDateToString())));
        assertThat(converter.parse("1970-01-06T18:53:20Z"), Matchers.is(date(1970, 1, 6, 18, 53, 20)));
        assertThat(converter.parse("1970-01-06T18:53:20.123Z"), Matchers.is(date(1970, 1, 6, 18, 53, 20, 123)));
    }

    @Test
    public void triesMultipleFormatsBeforeGivingUp() throws Exception {
        DateFormatConverter converter = new DateFormatConverter(Dates.RFC3339().formats().join(sequence(Dates.RFC822(), Dates.javaUtilDateToString())));
        assertThat(converter.parse("2011-07-19T12:43:26Z"), Matchers.is(date(2011, 7, 19, 12, 43, 26)));
        assertThat(converter.parse("Thu, 08 Sep 2011 07:14:14 GMT"), Matchers.is(date(2011, 9, 8, 7, 14, 14)));
        assertThat(converter.parse("Thu Sep 15 13:27:31 GMT 2011"), Matchers.is(date(2011, 9, 15, 13, 27, 31)));
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

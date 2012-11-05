package com.googlecode.totallylazy.time;

import org.hamcrest.Matchers;
import org.junit.Test;

import java.util.Date;

import static com.googlecode.totallylazy.time.Dates.date;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class DatesTest {
    @Test
    @SuppressWarnings("deprecation")
    public void whenCreatingDateFromLongSetTimezoneToUtc() throws Exception{
        assertThat(date(0).getTime(), Matchers.is(0L));
    }

    @Test
    public void canParseALexicalDate() throws Exception{
        Date result = Dates.LEXICAL().parse("20110908071414123");
        assertThat(result, Matchers.is(date(2011, 9, 8, 7, 14, 14, 123)));
    }

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
    public void canAddSecondsToDates() throws Exception {
        assertThat(Seconds.add(date(2000, 1, 1), 60), is(date(2000, 1, 1, 0, 0, 60)));
    }

    @Test
    public void canSubtractSecondsFromDates() throws Exception {
        assertThat(Seconds.subtract(date(2000, 1, 1), 60), is(date(1999, 12, 31, 23, 59, 0)));
    }

    @Test
    public void canAddDaysToDates() throws Exception {
        assertThat(Days.add(date(2000, 1, 1), 10), is(date(2000, 1, 11)));
    }

    @Test
    public void canSubtractDaysFromDates() throws Exception {
        assertThat(Days.subtract(date(2000, 1, 11), 10), is(date(2000, 1, 1)));
    }
}

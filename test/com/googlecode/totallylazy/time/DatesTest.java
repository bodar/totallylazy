package com.googlecode.totallylazy.time;

import org.hamcrest.Matchers;
import org.junit.Test;

import java.util.Calendar;
import java.util.Date;

import static com.googlecode.totallylazy.time.Dates.date;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.endsWith;
import static org.hamcrest.Matchers.is;

public class DatesTest {
    @Test
    @SuppressWarnings("deprecation")
    public void whenCreatingDateFromLongSetTimezoneToUtc() throws Exception{
        assertThat(date(0).getTime(), Matchers.is(0L));
    }

    @Test
    public void canParseAnApacheCommonLogFormatDate() throws Exception{
        Date result = Dates.APACHE().parse("03/Dec/2012:01:02:05 +0000");
        assertThat(result, Matchers.is(date(2012, 12, 3, 1, 2, 5)));
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

    @Test
    public void exposesConvenienceMethodsForGettingPartsOfDate() {
        assertThat(Dates.millisecond().apply(date(2013,1,1,0,0,0,123)), is(123));
        assertThat(Dates.second().apply(date(2013,1,1,0,0,59,0)), is(59));
        assertThat(Dates.minute().apply(date(2013,1,1,0,12,0,0)), is(12));
        assertThat(Dates.hourOfDay().apply(date(2013,1,1,23,0,0,0)), is(23));
        assertThat(Dates.dayOfWeek().apply(date(2013,1,15,0,0,0,0)), is(Calendar.TUESDAY));
        assertThat(Dates.dayOfMonth().apply(date(2013,1,15,0,0,0,0)), is(15));
        assertThat(Dates.weekOfMonth().apply(date(2013,12,31,0,0,0,0)), is(5));
        assertThat(Dates.month().apply(date(2013,12,1,0,0,0,0)), is(Calendar.DECEMBER));
        assertThat(Dates.dayOfYear().apply(date(2013,12,31,0,0,0,0)), is(365));
        assertThat(Dates.year().apply(date(2013,1,1,0,0,0,0)), is(2013));
        assertThat(Dates.calendarField(Calendar.MONTH).apply(date(2013,1,1,0,0,0,0)), is(Calendar.JANUARY));
    }

    @Test
    public void whenCreatingRFC822DateFromDateSetTimezoneToGMT() throws Exception {
        String result = Dates.RFC822().format(new Date(1200000000000L));
        assertThat(result, endsWith(" GMT"));
    }
}

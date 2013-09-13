package com.googlecode.totallylazy;

import com.googlecode.totallylazy.time.Dates;
import org.junit.Test;

import java.util.Date;

import static com.googlecode.totallylazy.Option.none;
import static com.googlecode.totallylazy.Sequences.sequence;
import static com.googlecode.totallylazy.matchers.Matchers.is;
import static com.googlecode.totallylazy.regex.Regex.regex;
import static java.lang.Integer.parseInt;
import static org.hamcrest.MatcherAssert.assertThat;

public class matchTest {
    @Test
    public void canDeconstructTypes() throws Exception {
        assertThat(new match<String, Date>(regex("(\\d{4})/(\\d{1,2})/(\\d{1,2})")) {
                Date value(String year, String month, String day) { return Dates.date(parseInt(year), parseInt(month), parseInt(day)); }
        }.apply("1977/1/10").get(), is(Dates.date(1977, 1, 10)));
    }

    @Test
    public void worksWithInstances() throws Exception {
        assertThat(new match<String,String>() {
            String value(String s) { return "String processed"; }
            String value(CharSequence s) { return "CharSequence processed"; }
            String value(Integer s) { return "Integer processed"; }
        }.apply("A String").get(), is("String processed"));
    }

    @Test
    public void supportsNoMatch() throws Exception {
        assertThat(new match<String,String>() {
            String value(Integer s) { return "Integer processed"; }
        }.apply("A String").getOrElse("No match found"), is("No match found"));
    }
}

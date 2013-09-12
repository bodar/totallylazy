package com.googlecode.totallylazy;

import com.googlecode.totallylazy.time.Dates;
import org.junit.Test;

import java.util.Date;

import static com.googlecode.totallylazy.matchers.Matchers.is;
import static com.googlecode.totallylazy.regex.Regex.regex;
import static java.lang.Integer.parseInt;
import static org.hamcrest.MatcherAssert.assertThat;

public class patternTest {
    @Test
    public void canDeconstructTypes() throws Exception {
        assertThat(new pattern("1977/1/10") {{
            new extract(regex("(\\d{4})/(\\d{1,2})/(\\d{1,2})")) {
                Date match(String year, String month, String day) { return Dates.date(parseInt(year), parseInt(month), parseInt(day)); }
            };
        }}.<Date>match(), is(Dates.date(1977, 1, 10)));
    }

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

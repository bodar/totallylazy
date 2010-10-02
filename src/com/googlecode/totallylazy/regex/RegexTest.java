package com.googlecode.totallylazy.regex;

import com.googlecode.totallylazy.Callable1;
import org.junit.Test;

import java.util.regex.MatchResult;

import static com.googlecode.totallylazy.regex.Regex.regex;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class RegexTest {
    @Test
    public void supportsReplacingText() throws Exception {
        String result = regex("\\d+").matches("Tel:123").replace(new Callable1<MatchResult, String>() {
            public String call(MatchResult s) throws Exception {
                return "321";
            }
        });
        assertThat(result, is("Tel:321"));
    }
}

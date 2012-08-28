package com.googlecode.totallylazy.regex;

import com.googlecode.totallylazy.Sequence;
import org.junit.Test;

import static com.googlecode.totallylazy.Functions.returns1;
import static com.googlecode.totallylazy.matchers.IterableMatcher.hasExactly;
import static com.googlecode.totallylazy.regex.Regex.regex;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class RegexTest {
    @Test
    public void supportsReplacingText() throws Exception {
        String result = regex("\\d+").findMatches("Tel:123").replace(returns1("321"));
        assertThat(result, is("Tel:321"));
    }

    @Test
    public void supportsSplittingText() throws Exception {
        Sequence<String> result = regex("\\s").split("The quick brown fox jumps over the lazy dog");
        assertThat(result, hasExactly("The", "quick", "brown", "fox", "jumps", "over", "the", "lazy", "dog"));
    }
}

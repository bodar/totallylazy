package com.googlecode.totallylazy.regex;

import com.googlecode.totallylazy.Sequence;
import org.junit.Test;

import static com.googlecode.totallylazy.Functions.constant;
import static com.googlecode.totallylazy.Functions.returns1;
import static com.googlecode.totallylazy.Sequences.sequence;
import static com.googlecode.totallylazy.matchers.IterablePredicates.hasExactly;
import static com.googlecode.totallylazy.Predicates.is;
import static com.googlecode.totallylazy.regex.Matches.functions.replace;
import static com.googlecode.totallylazy.regex.Regex.regex;
import static com.googlecode.totallylazy.Assert.assertThat;

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

    @Test
    public void supportsReplaceAll() throws Exception {
        Regex regex = regex("\\d+");
        Sequence<String> matches = sequence("111", "11 11", "AAA").map(regex.then(replace(constant("N"))));
        assertThat(matches, is(sequence("N", "N N", "AAA")));
    }
}

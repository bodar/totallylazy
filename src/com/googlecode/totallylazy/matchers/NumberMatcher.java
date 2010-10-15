package com.googlecode.totallylazy.matchers;

import com.googlecode.totallylazy.numbers.Numbers;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

public class NumberMatcher extends TypeSafeMatcher<Number> {
    private final Number other;

    public NumberMatcher(Number other) {
        this.other = other;
    }

    public void describeTo(Description description) {
        description.appendValue(other);
    }

    @Override
    protected boolean matchesSafely(Number number) {
        return Numbers.equalTo(number, other);
    }

    public static Matcher<? super Number> is(final Number other) {
        return new NumberMatcher(other);
    }

}

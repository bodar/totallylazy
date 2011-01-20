package com.googlecode.totallylazy.matchers;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

import static com.googlecode.totallylazy.Sequences.sequence;

public class HasSizeMatcher extends TypeSafeMatcher<Iterable> {
    private final Number size;

    public HasSizeMatcher(Number size) {
        this.size = size;
    }

    @Override
    protected boolean matchesSafely(Iterable iterable) {
        return size.equals(sequence(iterable).size());
    }

    public void describeTo(Description description) {
        description.appendText("Iterable with size " + size);
    }
}

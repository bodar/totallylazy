package com.googlecode.totallylazy;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.junit.internal.matchers.TypeSafeMatcher;

import java.util.Iterator;

import static com.googlecode.totallylazy.Sequences.sequence;

public class HasExactlyMatcher<T> extends TypeSafeMatcher<Iterable<T>> {
    private final Sequence<T> expected;

    private HasExactlyMatcher(Sequence<T> expected) {
        this.expected = expected;
    }

    public static <T> Matcher<Iterable<T>> hasExactly(T... items) {
        return hasExactly(sequence(items));
    }

    public static <T> Matcher<Iterable<T>> hasExactly(Sequence<T> expected) {
        return new HasExactlyMatcher<T>(expected);
    }

    @Override
    public boolean matchesSafely(Iterable<T> actual) {
        Iterator<T> e = this.expected.iterator();
        Iterator<T> a = actual.iterator();
        while(e.hasNext()){
            if(!a.hasNext()){
                return false;
            }

            if(!e.next().equals(a.next())){
                return false;
            }
        }
        if(a.hasNext()){
            return false;
        }
        return true;
    }

    public void describeTo(Description description) {
        description.appendValue(expected);
    }
}

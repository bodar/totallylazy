package com.googlecode.totallylazy.matchers;

import com.googlecode.totallylazy.Objects;
import com.googlecode.totallylazy.Sequence;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import java.util.Iterator;

import static com.googlecode.totallylazy.Sequences.sequence;

@SuppressWarnings("unchecked")
public class IterableMatcher<T> extends TypeSafeMatcher<Iterable<T>> {
    private final Sequence<T> expected;
    private boolean shouldBeSameSize;

    private IterableMatcher(Sequence<T> expected, boolean shouldBeSameSize) {
        this.expected = expected;
        this.shouldBeSameSize = shouldBeSameSize;
    }

    public static Matcher<Iterable> hasSize(final Number size) {
        return new HasSizeMatcher(size);
    }

    public static <T> Matcher<Iterable<T>> isEmpty(Class<T> aClass) {
        return IterableMatcher.<T>isEmpty();
    }

    public static <T> Matcher<Iterable<T>> isEmpty() {
        return IterableMatcher.<T>hasExactly();
    }

    public static <T> Matcher<Iterable<T>> hasExactly(final T first) {
        return hasExactly(sequence(first));
    }

    public static <T> Matcher<Iterable<T>> hasExactly(final T first, final T second) {
        return hasExactly(sequence(first, second));
    }

    public static <T> Matcher<Iterable<T>> hasExactly(final T first, final T second, final T third) {
        return hasExactly(sequence(first, second, third));
    }

    public static <T> Matcher<Iterable<T>> hasExactly(final T first, final T second, final T third, final T fourth) {
        return hasExactly(sequence(first, second, third, fourth));
    }

    public static <T> Matcher<Iterable<T>> hasExactly(final T first, final T second, final T third, final T fourth, final T fifth) {
        return hasExactly(sequence(first, second, third, fourth, fifth));
    }

    public static <T> Matcher<Iterable<T>> hasExactly(final T... items) {
        return hasExactly(sequence(items));
    }

    public static <T> Matcher<Iterable<T>> hasExactly(Sequence<T> expected) {
        return new IterableMatcher<T>(expected, true);
    }

    public static <T> Matcher<Iterable<T>> startsWith(T... items) {
        return startsWith(sequence(items));
    }

    public static <T> Matcher<Iterable<T>> startsWith(Sequence<T> expected) {
        return new IterableMatcher<T>(expected, false);
    }

    @Override
    public boolean matchesSafely(Iterable<T> actual) {
        Iterator<T> e = this.expected.iterator();
        Iterator<T> a = actual.iterator();
        while(e.hasNext()){
            if(!a.hasNext()){
                return false;
            }

            if(!Objects.equalTo(e.next(), a.next())){
                return false;
            }
        }
        if(shouldBeSameSize && a.hasNext()){
            return false;
        }
        return true;
    }

    public void describeTo(Description description) {
        description.appendValue(expected);
    }

}

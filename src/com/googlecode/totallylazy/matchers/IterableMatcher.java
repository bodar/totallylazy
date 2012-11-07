package com.googlecode.totallylazy.matchers;

import com.googlecode.totallylazy.Objects;
import com.googlecode.totallylazy.Sequence;
import com.googlecode.totallylazy.Sequences;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import java.util.Iterator;

import static com.googlecode.totallylazy.Sequences.sequence;
import static com.googlecode.totallylazy.matchers.Matchers.are;

public class IterableMatcher<T> extends TypeSafeMatcher<Iterable<T>> {
    private final Sequence<Matcher<? super T>> expected;
    private boolean shouldBeSameSize;

    private IterableMatcher(Iterable<? extends Matcher<? super T>> expected, boolean shouldBeSameSize) {
        this.expected = sequence(expected);
        this.shouldBeSameSize = shouldBeSameSize;
    }

    @SuppressWarnings("unchecked")
    public static Matcher<Iterable> hasSize(final Number size) {
        return new HasSizeMatcher(size);
    }

    public static <T> Matcher<Iterable<T>> isEmpty(Class<T> aClass) {
        return IterableMatcher.<T>isEmpty();
    }

    public static <T> Matcher<Iterable<T>> isEmpty() {
        return IterableMatcher.<T>hasExactly(Sequences.<T>empty());
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

    public static <T> Matcher<Iterable<T>> hasExactly(Iterable<T> expected) {
        return new IterableMatcher<T>(are(expected), true);
    }

    public static <T> Matcher<Iterable<T>> hasExactlyMatching(Iterable<? extends Matcher<? super T>> expected) {
        return new IterableMatcher<T>(expected, true);
    }

    public static <T> Matcher<Iterable<T>> startsWith(T... items) {
        return startsWith(sequence(items));
    }

    public static <T> Matcher<Iterable<T>> startsWith(Iterable<T> expected) {
        Iterable<? extends Matcher<T>> are = are(expected);
        return new IterableMatcher<T>(are, false);
    }

    @Override
    public boolean matchesSafely(Iterable<T> actual) {
        Iterator<Matcher<? super T>> e = this.expected.iterator();
        Iterator<T> a = actual.iterator();
        while(e.hasNext()){
            if(!a.hasNext()){
                return false;
            }

            if(!e.next().matches(a.next())){
                return false;
            }
        }
        if(shouldBeSameSize && a.hasNext()){
            return false;
        }
        return true;
    }

    public void describeTo(Description description) {
        description.appendValue("An iterable with values matching: ");
        description.appendValue(expected);
    }

}

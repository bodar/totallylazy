package com.googlecode.totallylazy.matchers;

import com.googlecode.totallylazy.Predicate;
import com.googlecode.totallylazy.predicates.LogicalPredicate;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

public class Matchers {
    public static <T> LogicalPredicate<T> predicate(final Matcher<T> matcher) {
        return new LogicalPredicate<T>() {
            public final boolean matches(T other) {
                return matcher.matches(other);
            }
        };
    }

    public static <T> Matcher<T> matcher(final Predicate<T> predicate) {
        return new TypeSafeMatcher<T>() {
            @Override
            protected boolean matchesSafely(T t) {
                return predicate.matches(t);
            }

            public void describeTo(Description description) {
                description.appendText(predicate.toString());
            }
        };
    }

    // fix broken Hamcrest 1.2 return type
    @SuppressWarnings("unchecked")
    public static <T> Matcher<T> is(T t) {
        return (Matcher<T>) org.hamcrest.Matchers.is(t);
    }
}

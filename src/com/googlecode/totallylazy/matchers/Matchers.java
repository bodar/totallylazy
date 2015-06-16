package com.googlecode.totallylazy.matchers;

import com.googlecode.totallylazy.Callable1;
import com.googlecode.totallylazy.Function;
import com.googlecode.totallylazy.Predicate;
import com.googlecode.totallylazy.predicates.LogicalPredicate;
import org.hamcrest.Description;
import org.hamcrest.DiagnosingMatcher;
import org.hamcrest.Matcher;
import org.hamcrest.SelfDescribing;
import org.hamcrest.StringDescription;
import org.hamcrest.TypeSafeMatcher;

import static com.googlecode.totallylazy.Callables.returns1;
import static com.googlecode.totallylazy.Sequences.sequence;
import static com.googlecode.totallylazy.Unchecked.cast;
import static org.hamcrest.StringDescription.asString;

public class Matchers {

    public static <T> Iterable<Matcher<T>> are(final Iterable<T> values, Class<T> clazz) {
        return are(values);
    }

    public static <T> Iterable<Matcher<T>> are(final Iterable<T> values) {
        return sequence(values).map(Matchers.<T>isMatcher());
    }

    public static Function<SelfDescribing, String> description() {
        return new Function<SelfDescribing, String>() {
            @Override
            public String call(SelfDescribing selfDescribing) throws Exception {
                return asString(selfDescribing);
            }
        };
    }

    public static <T> Function<T, String> describeMismatch(Class<T> type, final Matcher<? super T> matcher) {
        return describeMismatch(matcher);
    }

    public static <T> Function<T, String> describeMismatch(final Matcher<? super T> matcher) {
        if (matcher instanceof DiagnosingMatcher)
            return diagnoseMismatch((DiagnosingMatcher) matcher);
        return returns1(StringDescription.asString(matcher));
    }

    public static <T> Function<T, String> diagnoseMismatch(Class<T> type, final DiagnosingMatcher matcher) {
        return diagnoseMismatch(matcher);
    }

    public static <T> Function<T, String> diagnoseMismatch(final DiagnosingMatcher matcher) {
        return new Function<T, String>() {
            @Override
            public String call(T t) throws Exception {
                StringDescription mismatchDescription = new StringDescription();
                matcher.describeMismatch(t, mismatchDescription);
                return mismatchDescription.toString();
            }
        };
    }

    public static <T> Callable1<T, Matcher<T>> isMatcher(Class<T> clazz) {
        return isMatcher();
    }

    public static <T> Callable1<T, Matcher<T>> isMatcher() {
        return new Callable1<T, Matcher<T>>() {
            @Override
            public Matcher<T> call(T t) throws Exception {
                return is(t);
            }
        };
    }

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
    public static <T> Matcher<T> is(T t) {
        return cast(org.hamcrest.Matchers.is(t));
    }
}

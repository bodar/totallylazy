package com.googlecode.totallylazy.matchers;

import com.googlecode.totallylazy.Callable1;
import com.googlecode.totallylazy.Function1;
import com.googlecode.totallylazy.Predicate;
import com.googlecode.totallylazy.predicates.LogicalPredicate;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.SelfDescribing;
import org.hamcrest.TypeSafeMatcher;

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

    public static Function1<SelfDescribing, String> description() {
        return new Function1<SelfDescribing, String>() {
            @Override
            public String call(SelfDescribing selfDescribing) throws Exception {
                return asString(selfDescribing);
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

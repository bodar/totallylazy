package com.googlecode.totallylazy.predicates;

import com.googlecode.totallylazy.Predicate;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@SuppressWarnings("unchecked")
public class TypeSafePredicateTest {
    @Test
    public void willReturnFalseWhenPassedTheWrongClass() {
        Predicate predicate = new StringTypeSafePredicate();
        assertThat(predicate.matches(1), is(false));
    }

    @Test
    public void willPassThroughNullValues() {
        assertThat(new StringTypeSafePredicate().matches(null), is(true));
    }

    @Test
    public void willPassThroughValuesOfTheCorrectType() {
        assertThat(new StringTypeSafePredicate().matches("some string"), is(true));
    }

    private static class StringTypeSafePredicate extends TypeSafePredicate<String> {
        @Override
        protected boolean matchesSafely(String other) {
            return true;
        }
    }
}

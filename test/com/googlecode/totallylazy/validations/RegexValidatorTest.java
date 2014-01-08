package com.googlecode.totallylazy.validations;

import org.junit.Test;

import static com.googlecode.totallylazy.matchers.IterableMatcher.hasExactly;
import static com.googlecode.totallylazy.matchers.IterableMatcher.isEmpty;
import static com.googlecode.totallylazy.validations.ValidationResult.DEFAULT_KEY;
import static com.googlecode.totallylazy.validations.Validators.matchesRegex;
import static org.hamcrest.MatcherAssert.assertThat;

public class RegexValidatorTest {
    @Test
    public void doesNotMatchNull() {
        RegexValidator validator = matchesRegex("[A]");

        assertThat(
                validator.validate(null).messages(DEFAULT_KEY),
                hasExactly("Value must match pattern: [A]"));
    }

    @Test
    public void matchesBasedOnPattern() {
        RegexValidator validator = matchesRegex("[A]");

        assertThat(
                validator.validate("B").messages(DEFAULT_KEY),
                hasExactly("Value must match pattern: [A]"));

        assertThat(
                validator.validate("A").messages(DEFAULT_KEY),
                isEmpty(String.class));
    }
}

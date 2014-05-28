package com.googlecode.totallylazy.validations;

import org.junit.Test;

import static com.googlecode.totallylazy.Callables.returnArgument;
import static com.googlecode.totallylazy.Strings.contains;
import static com.googlecode.totallylazy.matchers.IterablePredicates.hasExactly;
import static com.googlecode.totallylazy.validations.Validators.validateThat;
import static com.googlecode.totallylazy.Assert.assertThat;

public class LogicalValidatorTest {
    @Test
    public void allowsChainingValidationsUsingFirstFailure() {
        LogicalValidator<String> containsOne = validateThat(contains("not there")).
                withMessage("First validator").
                castValidator(String.class);

        LogicalValidator<String> validator = containsOne.
                andIfSo(validateThat(contains("not there")).withMessage("Second validator"));

        assertThat(
                validator.validate("some string").allMessages(),
                hasExactly("First validator"));
    }

    @Test
    public void allowsChainingValidationsUsingAllOf() {
        LogicalValidator<String> containsOne = validateThat(contains("not there")).
                withMessage("First validator").
                castValidator(String.class);

        LogicalValidator<String> validator = containsOne.
                and(validateThat(contains("not there")).
                        withMessage("Second validator"));

        assertThat(
                validator.validate("some string").allMessages(),
                hasExactly("First validator", "Second validator"));
    }

    @Test
    public void canReassignMessagesToANewKey() {
        LogicalValidator<String> validator = validateThat(contains("not there")).
                withMessage("Validation failure").
                assigningFailuresTo("A").
                castValidator(String.class);

        assertThat(
                validator.validate("some string").
                        messages("A"),
                hasExactly("Validation failure"));
    }

    @Test
    public void canReassignMessagesToANewKeyAndThenChangeTheMessage() {
        LogicalValidator<String> validator = validateThat(contains("not there")).
                assigningFailuresTo("A").
                withMessage("Validation failure").
                castValidator(String.class);

        assertThat(
                validator.validate("some string").
                        messages("A"),
                hasExactly("Validation failure"));
    }

    @Test
    public void allowsSettingOfMessageBasedOnValidatedValue() {
        LogicalValidator<String> validator = validateThat(contains("not there")).
                castValidator(String.class).
                withMessage(returnArgument(String.class));

        assertThat(
                validator.validate("some string").allMessages(),
                hasExactly("some string"));
    }
}

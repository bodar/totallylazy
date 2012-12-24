package com.googlecode.totallylazy.validations;

import com.googlecode.totallylazy.Callable1;
import org.junit.Test;

import static com.googlecode.totallylazy.Callables.returnArgument;
import static com.googlecode.totallylazy.matchers.IterableMatcher.hasExactly;
import static com.googlecode.totallylazy.validations.Validators.validateThat;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.internal.matchers.StringContains.containsString;

public class LogicalValidatorTest {
    @Test
    public void allowsChainingValidationsUsingFirstFailure() {
        LogicalValidator<String> containsOne = validateThat(containsString("not there")).
                withMessage("First validator").
                castValidator(String.class);

        LogicalValidator<String> validator = containsOne.
                andIfSo(validateThat(containsString("not there")).withMessage("Second validator"));

        assertThat(
                validator.validate("some string").allMessages(),
                hasExactly("First validator"));
    }

    @Test
    public void allowsChainingValidationsUsingAllOf() {
        LogicalValidator<String> containsOne = validateThat(containsString("not there")).
                withMessage("First validator").
                castValidator(String.class);

        LogicalValidator<String> validator = containsOne.
                and(validateThat(containsString("not there")).
                        withMessage("Second validator"));

        assertThat(
                validator.validate("some string").allMessages(),
                hasExactly("First validator", "Second validator"));
    }

    @Test
    public void canReassignMessagesToANewKey() {
        LogicalValidator<String> validator = validateThat(containsString("not there")).
                withMessage("Validation failure").
                assigningFailuresTo("A").
                castValidator(String.class);

        assertThat(
                validator.validate("some string").
                        messages("A"),
                hasExactly("Validation failure"));
    }

    @Test
    public void allowsSettingOfMessageBasedOnValidatedValue() {
        LogicalValidator<String> validator = validateThat(containsString("not there")).
                castValidator(String.class).
                withMessage(returnArgument(String.class));

        assertThat(
                validator.validate("some string").allMessages(),
                hasExactly("some string"));
    }

}

package com.googlecode.totallylazy.validations;

import org.junit.Test;

import static com.googlecode.totallylazy.matchers.IterableMatcher.hasExactly;
import static com.googlecode.totallylazy.validations.Validators.firstFailure.firstFailure;
import static com.googlecode.totallylazy.validations.Validators.validateThat;
import static junit.framework.Assert.assertTrue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;

public class FirstFailureValidatorTest {
    @Test
    public void passesIfAllValidatorsPass() {
        Validator<String> validator = firstFailure(
                validateThat(containsString("one")),
                validateThat(containsString("two")));

        assertTrue("Validation should pass", validator.validate("one two").succeeded());
    }

    @Test
    public void onlyReturnsTheFirstFailure() {
        Validator<String> validator = firstFailure(
                validateThat(containsString("not there")).withMessage("first failure"),
                validateThat(containsString("also not there")).withMessage("second failure"));

        assertThat(validator.validate("some string").allMessages(), hasExactly("first failure"));
    }
}

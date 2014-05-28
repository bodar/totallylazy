package com.googlecode.totallylazy.validations;

import org.junit.Test;

import static com.googlecode.totallylazy.PredicateAssert.assertThat;
import static com.googlecode.totallylazy.Strings.contains;
import static com.googlecode.totallylazy.matchers.IterablePredicates.hasExactly;
import static com.googlecode.totallylazy.validations.Validators.firstFailure.firstFailure;
import static com.googlecode.totallylazy.validations.Validators.validateThat;
import static org.junit.Assert.assertTrue;

public class FirstFailureValidatorTest {
    @Test
    public void passesIfAllValidatorsPass() {
        Validator<String> validator = firstFailure(
                validateThat(contains("one")),
                validateThat(contains("two")));

        assertTrue("Validation should pass", validator.validate("one two").succeeded());
    }

    @Test
    public void onlyReturnsTheFirstFailure() {
        Validator<String> validator = firstFailure(
                validateThat(contains("not there")).withMessage("first failure"),
                validateThat(contains("also not there")).withMessage("second failure"));

        assertThat(validator.validate("some string").allMessages(), hasExactly("first failure"));
    }
}

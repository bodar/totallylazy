package com.googlecode.totallylazy.validations;

import org.junit.Test;

import static com.googlecode.totallylazy.matchers.IterableMatcher.hasExactly;
import static com.googlecode.totallylazy.validations.Validators.allOf.allOf;
import static com.googlecode.totallylazy.validations.Validators.validateThat;
import static junit.framework.Assert.assertTrue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;

public class AllOfValidatorTest {
    @Test
    public void passesIfAllValidatorsPass() {
        Validator<String> validator = allOf(
                validateThat(containsString("one")),
                validateThat(containsString("two")));

        assertTrue("Validation should pass", validator.validate("one two").succeeded());
    }

    @Test
    public void combinesManyValidationsIntoOne() {
        ValidationResult result = allOf(
                validateThat(containsString("one")).withMessage("First error message"),
                validateThat(containsString("two")).withMessage("Second error message"),
                validateThat(containsString("three")).withMessage("Third error message")).
                validate(" one ");

        assertThat(
                result.allMessages(),
                hasExactly("Second error message", "Third error message"));
    }

}

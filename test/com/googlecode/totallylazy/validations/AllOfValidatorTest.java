package com.googlecode.totallylazy.validations;

import org.junit.Test;

import static com.googlecode.totallylazy.PredicateAssert.assertThat;
import static com.googlecode.totallylazy.Strings.contains;
import static com.googlecode.totallylazy.matchers.IterablePredicates.hasExactly;
import static com.googlecode.totallylazy.validations.Validators.allOf.allOf;
import static com.googlecode.totallylazy.validations.Validators.validateThat;
import static junit.framework.Assert.assertTrue;

public class AllOfValidatorTest {
    @Test
    public void passesIfAllValidatorsPass() {
        Validator<String> validator = allOf(
                validateThat(contains("one")),
                validateThat(contains("two")));

        assertTrue("Validation should pass", validator.validate("one two").succeeded());
    }

    @Test
    public void combinesManyValidationsIntoOne() {
        ValidationResult result = allOf(
                validateThat(contains("one")).withMessage("First error message"),
                validateThat(contains("two")).withMessage("Second error message"),
                validateThat(contains("three")).withMessage("Third error message")).
                validate(" one ");

        assertThat(
                result.allMessages(),
                hasExactly("Second error message", "Third error message"));
    }

}

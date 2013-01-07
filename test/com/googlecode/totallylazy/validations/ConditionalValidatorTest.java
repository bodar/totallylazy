package com.googlecode.totallylazy.validations;

import org.junit.Test;

import static com.googlecode.totallylazy.Predicates.is;
import static com.googlecode.totallylazy.Predicates.not;
import static com.googlecode.totallylazy.validations.Validators.validateThat;
import static org.junit.Assert.assertTrue;

public class ConditionalValidatorTest {
    @Test
    public void returnsResultOfValidatorIfPredicateIsMatched() {
        LogicalValidator<String> conditionalValidator = validateThat(is("A")).
                when(not(is("B")));

        assertTrue(conditionalValidator.validate("C").failed());
    }

    @Test
    public void doesNotValidateIfPredicateIsMatched() {
        LogicalValidator<String> conditionalValidator = validateThat(is("A")).
                when(not(is("B")));

        assertTrue(conditionalValidator.validate("B").succeeded());
    }
}

package com.googlecode.totallylazy.validations;

import com.googlecode.totallylazy.predicates.LogicalPredicate;
import org.junit.Test;

import static com.googlecode.totallylazy.Predicates.is;
import static com.googlecode.totallylazy.validations.Validators.validateThat;
import static org.junit.Assert.assertTrue;

public class ConditionalValidatorTest {
    @Test
    public void returnsResultOfValidatorIfPredicateIsMatched() {
        LogicalPredicate<String> isB = is("B");
        LogicalValidator<String> conditionalValidator = validateThat(is("A")).
                when(isB);

        assertTrue(conditionalValidator.validate("B").failed());
    }

    @Test
    public void doesNotValidateIfPredicateIsNotMatched() {
        LogicalPredicate<String> isB = is("B");
        LogicalValidator<String> conditionalValidator = validateThat(is("A")).
                when(isB);

        assertTrue(conditionalValidator.validate("C").succeeded());
    }
}

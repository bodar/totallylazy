package com.googlecode.totallylazy.validations;

import org.junit.Test;

import java.util.Map;

import static com.googlecode.totallylazy.Maps.entries;
import static com.googlecode.totallylazy.Maps.map;
import static com.googlecode.totallylazy.matchers.IterablePredicates.hasExactly;
import static com.googlecode.totallylazy.matchers.IterablePredicates.isEmpty;
import static com.googlecode.totallylazy.validations.ValidationResult.constructors.failure;
import static com.googlecode.totallylazy.validations.ValidationResult.constructors.pass;
import static com.googlecode.totallylazy.validations.Validators.forAll;
import static com.googlecode.totallylazy.PredicateAssert.assertThat;

public class EveryItemValidatorTest {
    @Test
    public void appliesValidationToEachItemAndMergesTheResult() {
        Map<String, String> values = map(
                "one", "not valid",
                "two", "valid",
                "three", "not valid"
                );

        // EveryItemValidator is called inside Validators.forAll().
        // This is the typical pattern of usage, rather than constructing
        // an EveryItemValidator directly.
        ValidationResult result = forAll(
                    entries(String.class, String.class),
                    hasValue("valid")).
                validate(values);

        assertThat(result.messages("one"), hasExactly("Not the right value"));
        assertThat(result.messages("two"), isEmpty(String.class));
        assertThat(result.messages("three"), hasExactly("Not the right value"));
    }

    private Validator<Map.Entry<String, String>> hasValue(final String expected) {
        return new LogicalValidator<Map.Entry<String, String>>() {
            @Override
            public ValidationResult validate(Map.Entry<String, String> entry) {
                return entry.getValue().equals(expected)
                        ? pass()
                        : failure(entry.getKey(), "Not the right value");
            }
        };
    }
}

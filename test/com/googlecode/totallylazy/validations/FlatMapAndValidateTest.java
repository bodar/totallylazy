package com.googlecode.totallylazy.validations;

import org.junit.Test;

import java.util.Map;

import static com.googlecode.totallylazy.Maps.entries;
import static com.googlecode.totallylazy.Maps.map;
import static com.googlecode.totallylazy.Pair.pair;
import static com.googlecode.totallylazy.matchers.IterableMatcher.hasExactly;
import static com.googlecode.totallylazy.matchers.IterableMatcher.isEmpty;
import static com.googlecode.totallylazy.validations.ValidationResult.constructors.failure;
import static com.googlecode.totallylazy.validations.ValidationResult.constructors.pass;
import static com.googlecode.totallylazy.validations.Validators.forAll;
import static org.hamcrest.MatcherAssert.assertThat;

public class FlatMapAndValidateTest {
    @Test
    public void appliesValidationToEachMappedItem() {
        Map<String, String> values = map(
                pair("one", "valid"),
                pair("two", "not valid"));

        ValidationResult result = forAll(
                    entries(String.class, String.class),
                    hasValue("valid")).
                validate(values);

        assertThat(result.messages("one"), isEmpty(String.class));
        assertThat(result.messages("two"), hasExactly("Not the right value"));
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

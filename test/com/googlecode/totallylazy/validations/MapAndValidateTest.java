package com.googlecode.totallylazy.validations;

import com.googlecode.totallylazy.Function;
import com.googlecode.totallylazy.Predicate;
import com.googlecode.totallylazy.predicates.AbstractPredicate;
import org.junit.Test;

import static com.googlecode.totallylazy.Predicates.greaterThan;
import static com.googlecode.totallylazy.matchers.IterableMatcher.hasExactly;
import static com.googlecode.totallylazy.matchers.IterableMatcher.isEmpty;
import static com.googlecode.totallylazy.validations.Validators.validateThat;
import static org.hamcrest.MatcherAssert.assertThat;

public class MapAndValidateTest {
    @Test
    public void appliesAMappingFunctionToTheOriginalValueAndValidatesTheResult() {
        LogicalValidator<String> integer = validateThat(isInteger()).
                castValidator(String.class).
                withMessage("Must be an integer");

        Validator<String> greaterThanZero = integer.andIfSo(validateThat(parseInteger(), greaterThan(0)).
                withMessage("Must be greater than 0"));

        assertThat(
                greaterThanZero.validate("NOT AN INTEGER").allMessages(),
                hasExactly("Must be an integer"));

        assertThat(
                greaterThanZero.validate("0").allMessages(),
                hasExactly("Must be greater than 0"));

        assertThat(
                greaterThanZero.validate("1").allMessages(),
                isEmpty(String.class));
    }

    private Predicate<String> isInteger() {
        return (value) -> {
            try{
                Integer.parseInt(value);
                return true;
            }catch (Throwable e){
                return false;
            }
        };
    }

    private Function<String, Integer> parseInteger() {
        return Integer::parseInt;
    }
}

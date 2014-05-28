package com.googlecode.totallylazy.validations;

import com.googlecode.totallylazy.Function;
import com.googlecode.totallylazy.predicates.LogicalPredicate;
import org.junit.Test;

import static com.googlecode.totallylazy.Predicates.greaterThan;
import static com.googlecode.totallylazy.matchers.IterablePredicates.hasExactly;
import static com.googlecode.totallylazy.matchers.IterablePredicates.isEmpty;
import static com.googlecode.totallylazy.validations.Validators.validateThat;
import static com.googlecode.totallylazy.Assert.assertThat;

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

    private LogicalPredicate<String> isInteger() {
        return new LogicalPredicate<String>() {
            @Override
            public boolean matches(String value) {
                try{
                    Integer.parseInt(value);
                    return true;
                }catch (Throwable e){
                    return false;
                }
            }
        };
    }

    private Function<String, Integer> parseInteger() {
        return new Function<String, Integer>() {
            @Override
            public Integer call(String s) throws Exception {
                return Integer.parseInt(s);
            }
        };
    }
}

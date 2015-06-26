package com.googlecode.totallylazy.validations;

import com.googlecode.totallylazy.Function1;
import com.googlecode.totallylazy.Predicate;
import com.googlecode.totallylazy.CurriedCombiner;

import static com.googlecode.totallylazy.validations.PredicateValidator.constructors.validatePredicate;
import static com.googlecode.totallylazy.validations.ValidationResult.constructors.pass;

public interface Validator<T> extends Predicate<T> {
    ValidationResult validate(T instance);

    class functions {
        public static <T> Function1<Validator<? super T>, ValidationResult> validateAgainst(final T value) {
            return validator -> validator.validate(value);
        }

        public static <T> CurriedCombiner<T, ValidationResult> validateWith(final Validator<? super T> validator) {
            return new CurriedCombiner<T, ValidationResult>() {
                @Override
                public ValidationResult call(ValidationResult validationResult, T instance) throws Exception {
                    return validationResult.merge(validator.validate(instance));
                }

                @Override
                public ValidationResult identity() {
                    return pass();
                }

                @Override
                public ValidationResult combine(ValidationResult a, ValidationResult b) throws Exception {
                    return a.merge(b);
                }
            };
        }

        public static <T> Function1<Validator<T>, Validator<T>> setFailureMessage(final String message) {
            return validator -> validatePredicate(validator, message);
        }

        public static <T> Function1<Validator<T>, Validator<T>> setFailureMessage(final Function1<T, String> message) {
            return validator -> validatePredicate(validator, message);
        }
    }
}

package com.googlecode.totallylazy.validations;

import com.googlecode.totallylazy.Function;
import com.googlecode.totallylazy.Function1;
import com.googlecode.totallylazy.Predicate;

import static com.googlecode.totallylazy.Callables.returns1;
import static com.googlecode.totallylazy.Callers.call;
import static com.googlecode.totallylazy.validations.PredicateValidator.constructors.validatePredicate;
import static com.googlecode.totallylazy.validations.ValidationResult.constructors.failure;
import static com.googlecode.totallylazy.validations.ValidationResult.constructors.pass;

public class PredicateValidator<T> extends LogicalValidator<T> {
    private final Predicate<? super T> predicate;
    private final Function<? super T, String> message;

    public PredicateValidator(Predicate<? super T> predicate, Function<? super T, String> message) {
        this.predicate = predicate;
        this.message = message;
    }

    @Override
    public ValidationResult validate(T instance) {
        return predicate.matches(instance)
                ? pass()
                : failure(call(message, instance));
    }

    public static class constructors {
        public static <T> PredicateValidator<T> validatePredicate(Predicate<? super T> predicate) {
            return validatePredicate(predicate, "Please provide a valid value");
        }

        public static <T> PredicateValidator<T> validatePredicate(Predicate<? super T> predicate, String message) {
            return validatePredicate(predicate, returns1(message));
        }

        public static <T> PredicateValidator<T> validatePredicate(Predicate<? super T> predicate, Function<? super T, String> message) {
            return new PredicateValidator<T>(predicate, message);
        }
    }

    public static class functions {
        public static <T> Function1<Predicate<? super T>, Validator<T>> predicateAsValidator(Class<T> type) {
            return predicateAsValidator();
        }

        public static <T> Function1<Predicate<? super T>, Validator<T>> predicateAsValidator() {
            return new Function1<Predicate<? super T>, Validator<T>>() {
                @Override
                public Validator<T> call(Predicate<? super T> predicate) throws Exception {
                    return validatePredicate(predicate);
                }
            };
        }
    }
}

package com.googlecode.totallylazy.validations;

import com.googlecode.totallylazy.Callable1;
import com.googlecode.totallylazy.Predicate;
import com.googlecode.totallylazy.Predicates;
import com.googlecode.totallylazy.Sequence;
import com.googlecode.totallylazy.Sequences;
import com.googlecode.totallylazy.Strings;
import com.googlecode.totallylazy.matchers.IterableMatcher;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;

import static com.googlecode.totallylazy.Sequences.sequence;
import static com.googlecode.totallylazy.validations.EveryItemValidator.constructors.everyItem;
import static com.googlecode.totallylazy.validations.MapAndValidate.constructors.mapAndValidate;
import static com.googlecode.totallylazy.validations.MatcherValidator.constructors.validateMatcher;
import static com.googlecode.totallylazy.validations.PredicateValidator.constructors.validatePredicate;
import static com.googlecode.totallylazy.validations.ValidationResult.constructors.failure;
import static com.googlecode.totallylazy.validations.ValidationResult.constructors.pass;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.not;

public class Validators {
    public static final String PLEASE_PROVIDE_A_VALUE = "Please provide a value";

    public static class allOf {
        public static <T> AllOfValidator<T> allOf(Validator<? super T> first, Validator<? super T> second) {
            return allOf(Sequences.<Validator<? super T>>sequence(first, second));
        }

        public static <T> AllOfValidator<T> allOf(Validator<? super T> first, Validator<? super T> second, Validator<? super T> third) {
            return allOf(Sequences.<Validator<? super T>>sequence(first, second, third));
        }

        public static <T> AllOfValidator<T> allOf(Validator<? super T> first, Validator<? super T> second, Validator<? super T> third, Validator<? super T> fourth) {
            return allOf(Sequences.<Validator<? super T>>sequence(first, second, third, fourth));
        }

        public static <T> AllOfValidator<T> allOf(Validator<? super T> first, Validator<? super T> second, Validator<? super T> third, Validator<? super T> fourth, Validator<? super T> fifth) {
            return allOf(Sequences.<Validator<? super T>>sequence(first, second, third, fourth, fifth));
        }

        public static <T> AllOfValidator<T> allOf(Validator<? super T>... validators) {
            return AllOfValidator.constructors.allOf(validators);
        }

        public static <T> AllOfValidator<T> allOf(Sequence<Validator<? super T>> validators) {
            return AllOfValidator.constructors.allOf(validators);
        }
    }

    public static class firstFailure {
        public static <T> FirstFailureValidator<T> firstFailure(Validator<? super T> first, Validator<? super T> second) {
            return firstFailure(Sequences.<Validator<? super T>>sequence(first, second));
        }

        public static <T> FirstFailureValidator<T> firstFailure(Validator<? super T> first, Validator<? super T> second, Validator<? super T> third) {
            return firstFailure(Sequences.<Validator<? super T>>sequence(first, second, third));
        }

        public static <T> FirstFailureValidator<T> firstFailure(Validator<? super T> first, Validator<? super T> second, Validator<? super T> third, Validator<? super T> fourth) {
            return firstFailure(Sequences.<Validator<? super T>>sequence(first, second, third, fourth));
        }

        public static <T> FirstFailureValidator<T> firstFailure(Validator<? super T> first, Validator<? super T> second, Validator<? super T> third, Validator<? super T> fourth, Validator<? super T> fifth) {
            return firstFailure(Sequences.<Validator<? super T>>sequence(first, second, third, fourth, fifth));
        }

        public static <T> FirstFailureValidator<T> firstFailure(Validator<? super T>... validators) {
            return firstFailure(sequence(validators));
        }

        public static <T> FirstFailureValidator<T> firstFailure(Sequence<Validator<? super T>> validators) {
            return FirstFailureValidator.constructors.firstFailure(validators);
        }
    }

    public static <T, R> MapAndValidate<T, ? extends Iterable<? extends R>> forAll(Callable1<? super T, ? extends Iterable<? extends R>> map, Validator<? super R> validator) {
        return mapAndValidate(map, everyItem(validator));
    }

    public static <T, R> MapAndValidate<T, R> validateThat(Callable1<? super T, ? extends R> map, Validator<? super R> validator) {
        return mapAndValidate(map, validator);
    }

    public static <T, R> MapAndValidate<T, R> validateThat(Callable1<? super T, ? extends R> map, Matcher<? super R> matcher) {
        return mapAndValidate(map, validateThat(matcher));
    }

    public static <T, R> MapAndValidate<T, R> validateThat(Callable1<? super T, ? extends R> map, Predicate<? super R> predicate) {
        return mapAndValidate(map, validateThat(predicate));
    }

    public static <T, R> MapAndValidate<T, R> validateThat(Callable1<? super T, ? extends R> map, Predicate<? super R> predicate, String message) {
        return mapAndValidate(map, validateThat(predicate, message));
    }

    public static <T, R> MapAndValidate<T, R> validateThat(Callable1<? super T, ? extends R> map, Predicate<? super R> predicate, Callable1<? super R, String> message) {
        return mapAndValidate(map, validateThat(predicate, message));
    }

    public static <T> MatcherValidator<T> validateThat(Matcher<? super T> matcher) {
        return validateMatcher(matcher);
    }

    public static <T> PredicateValidator<T> validateThat(Predicate<? super T> predicate) {
        return validatePredicate(predicate);
    }

    public static <T> PredicateValidator<T> validateThat(Predicate<? super T> predicate, String message) {
        return validatePredicate(predicate, message);
    }

    public static <T> PredicateValidator<T> validateThat(Predicate<? super T> predicate, Callable1<? super T, String> message) {
        return validatePredicate(predicate, message);
    }

    public static <T> LogicalValidator<T> all(Class<T> type) {
        return all();
    }

    public static <T> LogicalValidator<T> all() {
        return validateThat(anything());
    }

    public static <T> LogicalValidator<T> never(Class<T> type) {
        return never();
    }

    public static <T> LogicalValidator<T> never() {
        return validatePredicate(Predicates.never(), "This validation will always fail");
    }

    public static <T> LogicalValidator<Iterable<T>> isNotEmpty(Class<T> type) {
        return isNotEmpty();
    }

    public static <T> LogicalValidator<Iterable<T>> isNotEmpty() {
        MatcherValidator<Iterable<T>> notEmpty = validateMatcher(not(IterableMatcher.<T>isEmpty()));
        return notEmpty.withMessage(PLEASE_PROVIDE_A_VALUE);
    }

    public static <T> LogicalValidator<T> isNotNull(Class<T> type) {
        return isNotNull();
    }

    public static <T> LogicalValidator<T> isNotNull() {
        MatcherValidator<T> notNull = validateMatcher(not(Matchers.<T>nullValue()));
        return notNull.withMessage(PLEASE_PROVIDE_A_VALUE);
    }

    public static LogicalValidator<String> isNotBlank() {
        return new LogicalValidator<String>() {
            @Override
            public ValidationResult validate(String instance) {
                if (Strings.isBlank(instance))
                    return failure(PLEASE_PROVIDE_A_VALUE);
                return pass();
            }
        };
    }
}

package com.googlecode.totallylazy.validations;

import com.googlecode.totallylazy.Callable1;
import com.googlecode.totallylazy.Sequence;

import java.util.ArrayList;
import java.util.List;

import static com.googlecode.totallylazy.Functions.call;
import static com.googlecode.totallylazy.Functions.returns1;
import static com.googlecode.totallylazy.Sequences.sequence;
import static com.googlecode.totallylazy.validations.ValidationResult.constructors.failure;
import static com.googlecode.totallylazy.validations.ValidationResult.constructors.pass;

public class AnyOfValidator<T> extends LogicalValidator<T>{
    public static final String DEFAULT_MESSAGE = "Please provide a valid value";

    private final Sequence<Validator<? super T>> validators;

    private AnyOfValidator(Sequence<Validator<? super T>> validators) {
        this.validators = validators;
    }

    @Override
    public ValidationResult validate(T instance) {
        List<ValidationResult> failures = new ArrayList<ValidationResult>();
        for (Validator<? super T> validator : validators) {
            ValidationResult result = validator.validate(instance);
            if(result.succeeded())
                return pass();
            failures.add(result);
        }
        return failure(DEFAULT_MESSAGE);
    }

    public static class constructors {
        public static <T> AnyOfValidator<T> anyOf(Validator<? super T>... validators) {
            return anyOf(sequence(validators));
        }
        public static <T> AnyOfValidator<T> anyOf(Sequence<Validator<? super T>> validators) {
            return new AnyOfValidator<T>(validators);
        }
    }
}

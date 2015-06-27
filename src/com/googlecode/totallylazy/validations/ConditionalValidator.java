package com.googlecode.totallylazy.validations;

import com.googlecode.totallylazy.predicates.Predicate;

import static com.googlecode.totallylazy.validations.ValidationResult.constructors.pass;

public class ConditionalValidator<T> extends LogicalValidator<T>{
    private final Predicate<? super T> predicate;
    private final Validator<? super T> validator;

    private ConditionalValidator(Validator<? super T> validator, Predicate<? super T> predicate) {
        this.validator = validator;
        this.predicate = predicate;
    }

    @Override
    public ValidationResult validate(T instance) {
        if(predicate.matches(instance))
           return validator.validate(instance);
        return pass();
    }

    public static class constructors{
        public static <T> ConditionalValidator<T> conditionalValidator(Validator<? super T> validator, Predicate<? super T> predicate){
            return new ConditionalValidator<T>(validator, predicate);
        }
    }
}

package com.googlecode.totallylazy.validations;

import static com.googlecode.totallylazy.Sequences.sequence;
import static com.googlecode.totallylazy.validations.Validator.functions.validateWith;

public class EveryItemValidator<T> extends LogicalValidator<Iterable<? extends T>> {
    private final Validator<? super T> itemValidator;

    public EveryItemValidator(Validator<? super T> itemValidator) {this.itemValidator = itemValidator;}

    @Override
    public ValidationResult validate(Iterable<? extends T> instance) {
        return sequence(instance).reduce(validateWith(itemValidator));
    }

    public static class constructors{
        public static <T> EveryItemValidator<T> everyItem(Validator<? super T> itemValidator, Class<T> type){
            return everyItem(itemValidator);
        }

        public static <T> EveryItemValidator<T> everyItem(Validator<? super T> itemValidator){
            return new EveryItemValidator<T>(itemValidator);
        }
    }
}

package com.googlecode.totallylazy.validations;

public class AssignFailuresToKey<T> extends LogicalValidator<T> {
    private final Validator<? super T> validator;
    private final String key;

    private AssignFailuresToKey(String key, Validator<? super T> validator) {
        this.key = key;
        this.validator = validator;
    }

    @Override
    public ValidationResult validate(T instance) {
        return validator.validate(instance).assignToKey(key);
    }

    static class constructors {
        public static <T> AssignFailuresToKey<T> assignFailuresToKey(String key, Validator<? super T> validator) {
            return new AssignFailuresToKey<T>(key, validator);
        }
    }
}

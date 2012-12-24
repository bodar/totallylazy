package com.googlecode.totallylazy.validations;

import com.googlecode.totallylazy.Function1;
import com.googlecode.totallylazy.Predicate;

public interface Validator<T> extends Predicate<T> {
    ValidationResult validate(T instance);

    class functions {
        public static <T> Function1<Validator<? super T>, ValidationResult> validateAgainst(final T value) {
            return new Function1<Validator<? super T>, ValidationResult>() {
                @Override
                public ValidationResult call(Validator<? super T> validator) throws Exception {
                    return validator.validate(value);
                }
            };
        }

//        public static <T> Function1<Validator<T>, Validator<T>> setFailureMessage(final String message){
//            return new Function1<Validator<T>, Validator<T>>() {
//                @Override
//                public Validator<T> call(Validator<T> validator) throws Exception {
//                    return validatePredicate(validator, message);
//                }
//            };
//        }
//
//        public static <T> Function1<Validator<T>, Validator<T>> setFailureMessage(final Callable1<T,String> message){
//            return new Function1<Validator<T>, Validator<T>>() {
//                @Override
//                public Validator<T> call(Validator<T> validator) throws Exception {
//                    return validatePredicate(validator, message);
//                }
//            };
//        }
    }
}
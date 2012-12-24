package com.googlecode.totallylazy.validations;

import com.googlecode.totallylazy.Callable1;

public abstract class LogicalValidator<T> extends AbstractValidator<T> {

    public LogicalValidator<T> and(Validator<? super T> validator){
        return Validators.allOf.allOf(this, validator); // Adding static imports to this class crashes javac 1.6
    }

    public LogicalValidator<T> andIfSo(Validator<? super T> validator){
        return Validators.firstFailure.firstFailure(this, validator); // Adding static imports to this class crashes javac 1.6
    }

    public LogicalValidator<T> withMessage(String message){
        return PredicateValidator.constructors.validatePredicate(this, message); // Adding static imports to this class crashes javac 1.6
    }

    public LogicalValidator<T> withMessage(Callable1<T,String> message){
        return PredicateValidator.constructors.validatePredicate(this, message); // Adding static imports to this class crashes javac 1.6
    }

    public LogicalValidator<T> assigningFailuresTo(String key){
        return AssignFailuresToKey.constructors.assignFailuresToKey(key, this); // Adding static imports to this class crashes javac 1.6
    }
}

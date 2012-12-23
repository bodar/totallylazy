package com.googlecode.totallylazy.validations;

import com.googlecode.totallylazy.Callable1;

import static com.googlecode.totallylazy.validations.PredicateValidator.constructors.validatePredicate;
import static com.googlecode.totallylazy.validations.Validators.allOf.allOf;
import static com.googlecode.totallylazy.validations.Validators.firstFailure.firstFailure;

public abstract class LogicalValidator<T> extends AbstractValidator<T> {
	public LogicalValidator<T> and(Validator<? super T> validator){
		return allOf(this, validator);
	}

	public LogicalValidator<T> andIfSo(Validator<? super T> validator){
		return firstFailure(this, validator);
	}

	public LogicalValidator<T> withMessage(String message){
		return validatePredicate(this, message);
	}

	public LogicalValidator<T> withMessage(Callable1<? super T,String> message){
		return validatePredicate(this, message);
	}

    public LogicalValidator<T> assigningFailuresTo(String key){
        return AssignFailuresToKey.constructors.assignFailuresToKey(key, this);
    }
}

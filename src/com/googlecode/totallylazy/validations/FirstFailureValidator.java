package com.googlecode.totallylazy.validations;

import com.googlecode.totallylazy.Sequence;

import static com.googlecode.totallylazy.Predicates.not;
import static com.googlecode.totallylazy.Sequences.sequence;
import static com.googlecode.totallylazy.validations.ValidationResult.constructors.pass;
import static com.googlecode.totallylazy.validations.ValidationResult.functions.succeeded;
import static com.googlecode.totallylazy.validations.Validator.functions.validateAgainst;

public class FirstFailureValidator<T> extends LogicalValidator<T> {
	private final Sequence<Validator<? super T>> validators;

	public FirstFailureValidator(Sequence<Validator<? super T>> validators) {
		this.validators = validators;
	}

	@Override
	public ValidationResult validate(T instance) {
		return validators.map(validateAgainst(instance)).
				find(not(succeeded())).
				getOrElse(pass());
	}

	public static class constructors {
		public static <T> FirstFailureValidator<T> firstFailure(Iterable<Validator<? super T>> validators) {
			return new FirstFailureValidator<T>(sequence(validators));
		}

	}
}

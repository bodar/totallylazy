package com.googlecode.totallylazy.validations;

import com.googlecode.totallylazy.Callable1;
import com.googlecode.totallylazy.Callers;
import com.googlecode.totallylazy.Sequences;

public class FlatMapAndValidate<T, R> extends LogicalValidator<T> {
	private final Callable1<? super T, ? extends Iterable<R>> map;
	private final Validator<? super R> validator;

	public FlatMapAndValidate(Callable1<? super T, ? extends Iterable<R>> map, Validator<? super R> validator) {
		this.map = map;
		this.validator = validator;
	}

	@Override
	public ValidationResult validate(T instance) {
        Iterable<R> values = Callers.call(map, instance);
        return Sequences.sequence(values).reduce(functions.validateWith(validator));
	}

	public static class constructors{
		public static <T,R> FlatMapAndValidate<T, R> flatMapAndValidate(Callable1<? super T, ? extends Iterable<R>> map, Validator<? super R> validator) {
			return new FlatMapAndValidate<T, R>(map, validator);
		}
	}
}

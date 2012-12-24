package com.googlecode.totallylazy.validations;

import com.googlecode.totallylazy.Callable1;

import static com.googlecode.totallylazy.Callers.call;

public class MapAndValidate<T, R> extends LogicalValidator<T> {
	private final Callable1<T, R> map;
	private final Validator<? super R> validator;

	public MapAndValidate(Callable1<T, R> map, Validator<? super R> validator) {
		this.map = map;
		this.validator = validator;
	}

	@Override
	public ValidationResult validate(T instance) {
		R value = call(map, instance);
		return validator.validate(value);
	}

	public static class constructors{
		public static <T,R> MapAndValidate<T, R> mapAndValidate(Callable1<T, R> map, Validator<? super R> validator) {
			return new MapAndValidate<T, R>(map, validator);
		}
	}
}

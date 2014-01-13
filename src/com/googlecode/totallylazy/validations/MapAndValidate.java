package com.googlecode.totallylazy.validations;

import com.googlecode.totallylazy.Function;

public class MapAndValidate<T, R> extends LogicalValidator<T> {
	private final Function<? super T, ? extends R> map;
	private final Validator<? super R> validator;

	public MapAndValidate(Function<? super T, ? extends R> map, Validator<? super R> validator) {
		this.map = map;
		this.validator = validator;
	}

	@Override
	public ValidationResult validate(T instance) {
        R value = map.apply(instance);
		return validator.validate(value);
	}

	public static class constructors{
		public static <T,R> MapAndValidate<T, R> mapAndValidate(Function<? super T, ? extends R> map, Validator<? super R> validator) {
			return new MapAndValidate<T, R>(map, validator);
		}
	}
}

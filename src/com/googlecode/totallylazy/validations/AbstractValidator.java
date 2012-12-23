package com.googlecode.totallylazy.validations;

public abstract class AbstractValidator<T> implements Validator<T> {
	@Override
	public boolean matches(T value) {
		return validate(value).succeeded();
	}
}

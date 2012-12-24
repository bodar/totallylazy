package com.googlecode.totallylazy.validations;

import com.googlecode.totallylazy.Unchecked;

public abstract class AbstractValidator<T> implements Validator<T> {
    @Override
    public abstract ValidationResult validate(T t);

    @Override
    public boolean matches(T other) {
        return validate(other).succeeded();
    }

    /**
     * Use this method if Java 1.6 loses track of what T is.
     */
    public <C> LogicalValidator<C> castValidator(Class<C> type){
        return Unchecked.cast(this);
    }
}
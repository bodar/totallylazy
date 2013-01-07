package com.googlecode.totallylazy.validations;

import com.googlecode.totallylazy.Unchecked;

public abstract class AbstractValidator<T> implements Validator<T> {
    @Override
    public abstract ValidationResult validate(T instance);

    @Override
    public boolean matches(T instance) {
        return validate(instance).succeeded();
    }

    /**
     * Use this method if Java 1.6 loses track of what T is.
     */
    public <C> LogicalValidator<C> castValidator(Class<C> type){
        return Unchecked.cast(this);
    }
}

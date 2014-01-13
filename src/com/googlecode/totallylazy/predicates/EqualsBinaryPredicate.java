package com.googlecode.totallylazy.predicates;

import com.googlecode.totallylazy.Objects;

public class EqualsBinaryPredicate<T> extends LogicalBinaryPredicate<T> {
    private EqualsBinaryPredicate() {}

    public static <T> EqualsBinaryPredicate<T> is(Class<T> aClass) {return equalTo();}
    public static <T> EqualsBinaryPredicate<T> is() {return equalTo();}
    public static <T> EqualsBinaryPredicate<T> equalTo(Class<T> aClass) {return equalTo();}
    public static <T> EqualsBinaryPredicate<T> equalTo() {return new EqualsBinaryPredicate<T>();}

    @Override
    public LogicalBinaryPredicate<T> flip() {
        return this;
    }

    @Override
    public AbstractPredicate<T> apply(T t) {
        return EqualsPredicate.equalTo(t);
    }

    @Override
    public boolean matches(T a, T b) {
        return Objects.equalTo(a, b);
    }
}

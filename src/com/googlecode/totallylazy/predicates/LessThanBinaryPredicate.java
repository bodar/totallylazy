package com.googlecode.totallylazy.predicates;

public class LessThanBinaryPredicate<T extends Comparable<? super T>> extends LogicalBinaryPredicate<T> {
    public static <T extends Comparable<? super T>> LessThanBinaryPredicate<T> lessThan(Class<T> aClass) {return LessThanBinaryPredicate.<T>lessThan(); }
    public static <T extends Comparable<? super T>> LessThanBinaryPredicate<T> lessThan() {return new LessThanBinaryPredicate<T>();}

    @Override
    public LogicalBinaryPredicate<T> flip() {
        return GreaterThanBinaryPredicate.<T>greaterThan();
    }

    @Override
    public AbstractPredicate<T> apply(T t) {
        return GreaterThanPredicate.greaterThan(t);
    }

    @Override
    public boolean matches(T a, T b) {
        return a.compareTo(b) < 0;
    }
}

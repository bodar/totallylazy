package com.googlecode.totallylazy.predicates;

public class GreaterThanBinaryPredicate<T extends Comparable<? super T>> extends LogicalBinaryPredicate<T> {
    public static <T extends Comparable<? super T>> GreaterThanBinaryPredicate<T> greaterThan(Class<T> aClass) {return GreaterThanBinaryPredicate.<T>greaterThan(); }
    public static <T extends Comparable<? super T>> GreaterThanBinaryPredicate<T> greaterThan() {return new GreaterThanBinaryPredicate<T>();}

    @Override
    public LogicalBinaryPredicate<T> flip() {
        return LessThanBinaryPredicate.<T>lessThan();
    }

    @Override
    public LogicalPredicate<T> apply(T t) {
        return LessThanPredicate.lessThan(t);
    }

    @Override
    public boolean matches(T a, T b) {
        return a.compareTo(b) > 0;
    }
}

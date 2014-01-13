package com.googlecode.totallylazy;

public interface Predicate<T> extends Function<T, Boolean>, java.util.function.Predicate<T> {
    boolean matches(T other);

    @Override
    default Boolean call(T t) throws Exception {
        return matches(t);
    }

    @Override
    default boolean test(T t) {
        return matches(t);
    }

    default Predicate<T> and(Predicate<? super T> predicate) {
        return Predicates.<T>and(this, predicate);
    }

    default Predicate<T> or(Predicate<? super T> predicate) {
        return Predicates.<T>or(this, predicate);
    }

    default Predicate<T> not() {
        return Predicates.<T>not(this);
    }

    static <T> Predicate<T> predicate(final Function<? super T, Boolean> predicate) {
        if(predicate instanceof Predicate){
            return Unchecked.cast(predicate);
        }
        return other -> {
            try {
                return predicate.call(other);
            } catch (Exception e) {
                return false;
            }
        };
    }

}

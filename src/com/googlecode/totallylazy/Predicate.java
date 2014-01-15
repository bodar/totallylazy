package com.googlecode.totallylazy;

public interface Predicate<T> extends java.util.function.Predicate<T> {
    boolean matches(T other);

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

    @Override
    default Predicate<T> negate() {
        return not();
    }

    default Function<T, Boolean> function() {
        return this::matches;
    }

    static <T> Predicate<T> predicate(final Function<? super T, Boolean> predicate) {
        if(predicate instanceof Predicate){
            return Unchecked.cast(predicate);
        }
        return other -> {
            try {
                Boolean result = predicate.call(other);
                return result == null ? false : result;
            } catch (Exception e) {
                return false;
            }
        };
    }

}

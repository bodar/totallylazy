package com.googlecode.totallylazy;

import com.googlecode.totallylazy.predicates.*;
import org.hamcrest.Matcher;

import static com.googlecode.totallylazy.Sequences.primes;

public class Predicates {
    public static <T> Predicate<T> predicate(final Matcher<T> matcher) {
        return new Predicate<T>() {
            public boolean matches(T other) {
                return matcher.matches(other);
            }
        };
    }


    public static <T> Predicate<T> instanceOf(final Class t) {
        return new InstanceOf<T>(t);
    }

    public static <T> Predicate<T> is(final T t) {
        return new Is<T>(t);
    }

    public static <T> Predicate<T> not(final T t) {
        return new Not<T>(is(t));
    }
    public static <T> Predicate<T> not(final Predicate<T> t) {
        return new Not<T>(t);
    }

    public static <T> Predicate<T> countTo(final int count) {
        return new CountTo<T>(count);
    }

    public static <T> Predicate<T> whileTrue(final Predicate<T> t) {
        return new WhileTrue<T>(t);
    }

    public static Predicate<Integer> even() {
        return remainderIs(2, 0);
    }

    public static Predicate<Integer> odd() {
        return remainderIs(2, 1);
    }

    public static Predicate<Integer> prime() {
        return new Predicate<Integer>() {
            public boolean matches(Integer candidate) {
                return primes().takeWhile(primeSquaredLessThan(candidate)).forAll(not(remainderIsZero(candidate)));
            }
        };
    }

    public static Predicate<Integer> primeSquaredLessThan(final Integer candidate) {
        return new Predicate<Integer>() {
            public boolean matches(Integer prime) {
                return (prime * prime) <= candidate ;
            }
        };
    }

    public static Predicate<Integer> remainderIsZero(final Integer divisor) {
        return new Predicate<Integer>() {
            public boolean matches(Integer prime) {
                return divisor % prime == 0;
            }
        };
    }

    public static Predicate<Integer> remainderIs(int divisor, int remainder) {
        return new RemainderIs(divisor, remainder);
    }

    public static <T> Predicate<T> notNull(Class<T> aClass) {
        return new NotNullPredicate<T>();
    }

    public static <T> Predicate<Option<T>> some() {
        return new Predicate<Option<T>>() {
            public boolean matches(Option<T> other) {
                return !other.isEmpty();
            }
        };
    }
}

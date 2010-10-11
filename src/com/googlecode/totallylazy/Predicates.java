package com.googlecode.totallylazy;

import com.googlecode.totallylazy.numbers.Numbers;
import com.googlecode.totallylazy.predicates.*;
import org.hamcrest.Matcher;

import static com.googlecode.totallylazy.Sequences.primes;
import static com.googlecode.totallylazy.numbers.Numbers.*;

public class Predicates {
    public static <T> Predicate<T> predicate(final Matcher<T> matcher) {
        return new Predicate<T>() {
            public boolean matches(T other) {
                return matcher.matches(other);
            }
        };
    }

    public static <T> Predicate<? super T> onlyOnce(final Predicate<? super T> predicate) {
        return new OnlyOnce<T>(predicate);
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
    public static <T> Predicate<T> not(final Predicate<? super T> t) {
        return new Not<T>(t);
    }

    public static <T> Predicate<T> countTo(final int count) {
        return new CountTo<T>(count);
    }

    public static <T> Predicate<T> whileTrue(final Predicate<? super T> t) {
        return new WhileTrue<T>(t);
    }

    public static Predicate<Number> even() {
        return remainderIs(2, 0);
    }

    public static Predicate<Number> odd() {
        return remainderIs(2, 1);
    }

    public static Predicate<Number> prime() {
        return new Predicate<Number>() {
            public boolean matches(Number candidate) {
                return primes().takeWhile(primeSquaredLessThan(candidate)).forAll(not(remainderIsZero(candidate)));
            }
        };
    }

    public static Predicate<Number> primeSquaredLessThan(final Number candidate) {
        return new Predicate<Number>() {
            public boolean matches(Number prime) {
                return Numbers.lessThanOrEqual(multiply(prime, prime),candidate);
            }
        };
    }

    public static Predicate<Number> remainderIsZero(final Number dividend) {
        return new Predicate<Number>() {
            public boolean matches(Number divisor) {
                return Numbers.isZero(remainder(dividend,divisor));
            }
        };
    }

    public static Predicate<Number> remainderIs(Number divisor, Number remainder) {
        return new RemainderIs(divisor, remainder);
    }

    public static <T> Predicate<T> notNull(Class<T> aClass) {
        return not(aNull(aClass));
    }

    public static <T> Predicate<T> aNull(Class<T> aClass) {
        return new Null<T>();
    }

    public static <T> Predicate<Option<T>> some() {
        return new Predicate<Option<T>>() {
            public boolean matches(Option<T> other) {
                return !other.isEmpty();
            }
        };
    }
}

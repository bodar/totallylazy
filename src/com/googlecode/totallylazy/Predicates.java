package com.googlecode.totallylazy;

import com.googlecode.totallylazy.numbers.Numbers;
import com.googlecode.totallylazy.predicates.*;
import org.hamcrest.Matcher;

import java.lang.reflect.Method;

import static com.googlecode.totallylazy.numbers.Numbers.*;

public class Predicates {
    public static <T> Predicate<T> predicate(final Matcher<T> matcher) {
        return new Predicate<T>() {
            public final boolean matches(T other) {
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

    public static <T> Predicate<? super T> and(final Predicate<? super T>... predicates) {
        return new Predicate<T>() {
            public boolean matches(T value) {
                for (Predicate<? super T> predicate : predicates) {
                    if (!predicate.matches(value)) return false;
                }
                return true;
            }
        };
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
            public final boolean matches(final Number candidate) {
                return primes().takeWhile(primeSquaredLessThan(candidate)).forAll(not(remainderIsZero(candidate)));
            }
        };
    }

    public static Predicate<Number> primeSquaredLessThan(final Number candidate) {
        return new Predicate<Number>() {
            public final boolean matches(final Number prime) {
                return Numbers.lessThanOrEqual(multiply(prime, prime),candidate);
            }
        };
    }

    public static Predicate<Number> remainderIsZero(final Number dividend) {
        return new Predicate<Number>() {
            public final boolean matches(Number divisor) {
                return Numbers.isZero(remainder(dividend,divisor));
            }
        };
    }

    public static Predicate<Number> remainderIs(final Number divisor, final Number remainder) {
        return new RemainderIs(divisor, remainder);
    }

    public static <T> Predicate<T> notNull(final Class<T> aClass) {
        return not(aNull(aClass));
    }

    public static <T> Predicate<T> aNull(final Class<T> aClass) {
        return new Null<T>();
    }

    public static <T> Predicate<Option<T>> some() {
        return new Predicate<Option<T>>() {
            public final boolean matches(Option<T> other) {
                return !other.isEmpty();
            }
        };
    }

    public static Predicate<Method> arguments(final Class<?>... expectedClasses) {
        return new Predicate<Method>() {
            public boolean matches(Method method) {
                final Class<?>[] actualClasses = method.getParameterTypes();
                if (actualClasses.length != expectedClasses.length) {
                    return false;
                }
                for (int i = 0; i < actualClasses.length; i++) {
                    Class<?> actual = actualClasses[i];
                    Class<?> expected = expectedClasses[i];
                    if(!actual.isAssignableFrom(expected)){
                        return false;
                    }
                }
                return true;
            }
        };

    }

    public static Predicate<Method> modifier(final int modifier) {
        return new Predicate<Method>() {
            public boolean matches(Method method) {
                return (method.getModifiers() & modifier) != 0;
            }
        };
    }

}

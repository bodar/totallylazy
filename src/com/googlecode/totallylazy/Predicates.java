package com.googlecode.totallylazy;

import com.googlecode.totallylazy.numbers.Numbers;
import com.googlecode.totallylazy.predicates.AndPredicate;
import com.googlecode.totallylazy.predicates.Between;
import com.googlecode.totallylazy.predicates.BetweenPredicate;
import com.googlecode.totallylazy.predicates.CountTo;
import com.googlecode.totallylazy.predicates.EqualsPredicate;
import com.googlecode.totallylazy.predicates.GreaterThanOrEqualToPredicate;
import com.googlecode.totallylazy.predicates.GreaterThanPredicate;
import com.googlecode.totallylazy.predicates.InPredicate;
import com.googlecode.totallylazy.predicates.InstanceOf;
import com.googlecode.totallylazy.predicates.LessThanOrEqualToPredicate;
import com.googlecode.totallylazy.predicates.LessThanPredicate;
import com.googlecode.totallylazy.predicates.Not;
import com.googlecode.totallylazy.predicates.Null;
import com.googlecode.totallylazy.predicates.OnlyOnce;
import com.googlecode.totallylazy.predicates.OrPredicate;
import com.googlecode.totallylazy.predicates.RemainderIs;
import com.googlecode.totallylazy.predicates.WherePredicate;
import com.googlecode.totallylazy.predicates.WhileTrue;
import org.hamcrest.Matcher;

import java.lang.reflect.Method;
import java.util.Collection;

import static com.googlecode.totallylazy.Sequences.sequence;
import static com.googlecode.totallylazy.numbers.Numbers.multiply;
import static com.googlecode.totallylazy.numbers.Numbers.primes;
import static com.googlecode.totallylazy.numbers.Numbers.remainder;

public class Predicates {
    public static <T> Predicate<Collection<? extends T>> contains(final T t) {
        return new Predicate<Collection<? extends T>>() {
            public boolean matches(Collection<? extends T> other) {
                return other.contains(t);
            }
        };
    }

    public static <T> Predicate<T> in(final T... values) {
        return in(sequence(values));
    }

    public static <T> Predicate<T> in(final Sequence<T> values) {
        return new InPredicate<T>(values);
    }

    public static Predicate<? super Either> isLeft() {
        return new Predicate<Either>() {
            public boolean matches(Either either) {
                return either.isLeft();
            }
        };
    }

    public static Predicate<? super Either> isRight() {
        return new Predicate<Either>() {
            public boolean matches(Either either) {
                return either.isRight();
            }
        };
    }

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
        return new EqualsPredicate<T>(t);
    }

    public static <T> Predicate<T> is(final Predicate<T> t) {
        return t;
    }

    public static <T> AndPredicate<T> and(final Predicate<? super T>... predicates) {
        return new AndPredicate<T>(predicates);
    }

    public static <T> OrPredicate<T> or(final Predicate<? super T>... predicates) {
        return new OrPredicate<T>(predicates);
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
                return Numbers.lessThanOrEqualTo(multiply(prime, prime), candidate);
            }
        };
    }

    public static Predicate<Number> remainderIsZero(final Number dividend) {
        return new Predicate<Number>() {
            public final boolean matches(Number divisor) {
                return Numbers.isZero(remainder(dividend, divisor));
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
                    if (!actual.isAssignableFrom(expected)) {
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

    public static Predicate<Object> assignableTo(final Class aClass) {
        return new Predicate<Object>() {
            public boolean matches(Object other) {
                return aClass.isAssignableFrom(other.getClass());
            }
        };
    }

    public static <T, R> WherePredicate<T, R> where(final Callable1<? super T, R> callable, final Predicate<? super R> predicate) {
        return new WherePredicate<T, R>(callable, predicate);
    }

    public static <T, R> WherePredicate<T, R> by(final Callable1<? super T, R> callable, final Predicate<? super R> predicate) {
        return where(callable, predicate);
    }

    public static <T> Predicate<? super Predicate<T>> matches(final T instance) {
        return new Predicate<Predicate<T>>() {
            public boolean matches(Predicate<T> predicate) {
                return predicate.matches(instance);
            }
        };
    }

    public static <T extends Comparable<T>> Predicate<T> greaterThan(final T comparable) {
        return new GreaterThanPredicate<T>(comparable);
    }

    public static <T extends Comparable<T>> Predicate<T> greaterThanOrEqualTo(final T comparable) {
        return new GreaterThanOrEqualToPredicate<T>(comparable);
    }

    public static <T extends Comparable<T>> Predicate<T> lessThan(final T comparable) {
        return new LessThanPredicate<T>(comparable);
    }

    public static <T extends Comparable<T>> Predicate<T> lessThanOrEqualTo(final T comparable) {
        return new LessThanOrEqualToPredicate<T>(comparable);
    }

    public static <T extends Comparable<T>> Between<T> between(final T lower, final T upper) {
        return new BetweenPredicate<T>(lower, upper);
    }

}

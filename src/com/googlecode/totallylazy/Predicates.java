package com.googlecode.totallylazy;

import com.googlecode.totallylazy.predicates.AlwaysFalse;
import com.googlecode.totallylazy.predicates.AlwaysTrue;
import com.googlecode.totallylazy.predicates.AndPredicate;
import com.googlecode.totallylazy.predicates.BetweenPredicate;
import com.googlecode.totallylazy.predicates.CountTo;
import com.googlecode.totallylazy.predicates.EqualsPredicate;
import com.googlecode.totallylazy.predicates.GreaterThanOrEqualToPredicate;
import com.googlecode.totallylazy.predicates.GreaterThanPredicate;
import com.googlecode.totallylazy.predicates.InPredicate;
import com.googlecode.totallylazy.predicates.InstanceOf;
import com.googlecode.totallylazy.predicates.LessThanOrEqualToPredicate;
import com.googlecode.totallylazy.predicates.LessThanPredicate;
import com.googlecode.totallylazy.predicates.LogicalPredicate;
import com.googlecode.totallylazy.predicates.Not;
import com.googlecode.totallylazy.predicates.NullPredicate;
import com.googlecode.totallylazy.predicates.OnlyOnce;
import com.googlecode.totallylazy.predicates.OrPredicate;
import com.googlecode.totallylazy.predicates.WherePredicate;
import com.googlecode.totallylazy.predicates.WhileTrue;

import java.util.Collection;

import static com.googlecode.totallylazy.Functions.function;
import static com.googlecode.totallylazy.Sequences.sequence;
import static com.googlecode.totallylazy.Sets.set;
import static com.googlecode.totallylazy.predicates.LogicalPredicate.logicalPredicate;

public class Predicates {
    public static <T> LogicalPredicate<T> alwaysTrue() {
        return AlwaysTrue.alwaysTrue();
    }

    public static <T> LogicalPredicate<T> alwaysTrue(Class<T> aClass) {
        return AlwaysTrue.alwaysTrue();
    }

    public static <T> LogicalPredicate<T> always() {
        return AlwaysTrue.alwaysTrue();
    }

    public static <T> LogicalPredicate<T> always(Class<T> aClass) {
        return AlwaysTrue.alwaysTrue();
    }

    public static <T> LogicalPredicate<T> all() {
        return AlwaysTrue.alwaysTrue();
    }

    public static <T> LogicalPredicate<T> all(Class<T> aClass) {
        return AlwaysTrue.alwaysTrue();
    }

    public static <T> LogicalPredicate<T> any() {
        return AlwaysTrue.alwaysTrue();
    }

    public static <T> LogicalPredicate<T> any(Class<T> aClass) {
        return AlwaysTrue.alwaysTrue();
    }

    public static <T> LogicalPredicate<T> anything() {
        return AlwaysTrue.alwaysTrue();
    }

    public static <T> LogicalPredicate<T> anything(Class<T> aClass) {
        return AlwaysTrue.alwaysTrue();
    }

    public static <T> LogicalPredicate<T> alwaysFalse() {
        return AlwaysFalse.alwaysFalse();
    }

    public static <T> LogicalPredicate<T> alwaysFalse(Class<T> aClass) {
        return AlwaysFalse.alwaysFalse();
    }

    public static <T> LogicalPredicate<T> never() {
        return AlwaysFalse.alwaysFalse();
    }

    public static <T> LogicalPredicate<T> never(Class<T> aClass) {
        return AlwaysFalse.alwaysFalse();
    }

    public static <T> LogicalPredicate<Collection<T>> contains(final T t) {
        return new LogicalPredicate<Collection<T>>() {
            public boolean matches(Collection<T> other) {
                return other.contains(t);
            }
        };
    }

    public static <T> LogicalPredicate<Iterable<T>> exists(final Predicate<? super T> predicate) {
        return new LogicalPredicate<Iterable<T>>() {
            public boolean matches(Iterable<T> iterable) {
                return sequence(iterable).exists(predicate);
            }
        };
    }

    public static <T> LogicalPredicate<Iterable<T>> forAll(final Predicate<? super T> predicate) {
        return new LogicalPredicate<Iterable<T>>() {
            public boolean matches(Iterable<T> iterable) {
                return sequence(iterable).forAll(predicate);
            }
        };
    }

    public static <T> LogicalPredicate<Iterable<T>> subsetOf(final Iterable<? extends T> superset) {
        return forAll(in(superset));
    }

    public static <T> LogicalPredicate<Iterable<T>> supersetOf(final Iterable<? extends T> subset) {
        return new LogicalPredicate<Iterable<T>>() {
            public boolean matches(Iterable<T> superset) {
                return sequence(subset).forAll(in(superset));
            }
        };
    }

    public static <T> LogicalPredicate<Iterable<T>> setEqualityWith(final Iterable<? extends T> other) {
        return new LogicalPredicate<Iterable<T>>() {
            public boolean matches(Iterable<T> iterable) {
                return set(iterable).equals(set(other));
            }
        };
    }

    @SafeVarargs
    public static <T> LogicalPredicate<T> in(final T... values) {
        return in(sequence(values));
    }

    public static <T> LogicalPredicate<T> in(final Iterable<? extends T> values) {
        return InPredicate.in(values);
    }

    public static <T> LogicalPredicate<T> in(final Collection<? extends T> values) {
        return InPredicate.in(values);
    }

    public static LogicalPredicate<Either> isLeft() {
        return new LogicalPredicate<Either>() {
            public boolean matches(Either either) {
                return either.isLeft();
            }
        };
    }

    public static LogicalPredicate<Either> isRight() {
        return new LogicalPredicate<Either>() {
            public boolean matches(Either either) {
                return either.isRight();
            }
        };
    }

    public static <T> LogicalPredicate<T> onlyOnce(final Predicate<? super T> predicate) {
        return new OnlyOnce<T>(predicate);
    }

    public static <T> LogicalPredicate<T> instanceOf(final Class<?> t) {
        return new InstanceOf<T>(t);
    }

    public static <T> LogicalPredicate<T> equalTo(final T t) {
        return EqualsPredicate.equalTo(t);
    }

    public static <T> LogicalPredicate<T> is(final T t) {
        return equalTo(t);
    }

    public static <S, T extends Predicate<S>> T is(final T t) {
        return t;
    }

    public static <T> LogicalPredicate<T> and() {
        return AndPredicate.and(Sequences.<Predicate<T>>empty());
    }

    public static <T> LogicalPredicate<T> and(final Predicate<? super T> first) {
        return logicalPredicate(first);
    }

    @SafeVarargs
    public static <T> LogicalPredicate<T> and(final Predicate<? super T>... predicates) {
        return and(sequence(predicates));
    }

    public static <T> LogicalPredicate<T> and(final Iterable<? extends Predicate<? super T>> predicates) {
        return AndPredicate.and(predicates);
    }

    public static <T> LogicalPredicate<T> or() {
        return OrPredicate.or(Sequences.<Predicate<T>>empty());
    }

    public static <T> LogicalPredicate<T> or(final Predicate<? super T> first) {
        return logicalPredicate(first);
    }

    @SafeVarargs
    public static <T> LogicalPredicate<T> or(final Predicate<? super T>... predicates) {
        return or(sequence(predicates));
    }

    public static <T> LogicalPredicate<T> or(final Iterable<? extends Predicate<? super T>> predicates) {
        return OrPredicate.or(predicates);
    }

    public static <T> LogicalPredicate<T> not(final T t) {
        return not(is(t));
    }

    public static <T> LogicalPredicate<T> not(final Predicate<? super T> t) {
        return Not.not(t);
    }

    public static LogicalPredicate<Object> countTo(final Number count) {
        return new CountTo(count);
    }

    public static <T> LogicalPredicate<T> whileTrue(final Predicate<? super T> t) {
        return new WhileTrue<T>(t);
    }

    public static <T> LogicalPredicate<T> nullValue() {
        return new NullPredicate<T>();
    }

    public static <T> LogicalPredicate<T> nullValue(final Class<T> type) {
        return nullValue();
    }

    public static <T> LogicalPredicate<T> notNullValue() {
        return not(nullValue());
    }

    public static <T> LogicalPredicate<T> notNullValue(final Class<T> aClass) {
        return notNullValue();
    }

    @Deprecated // will be removed after 950. Use flatMap instead.
    public static <T> LogicalPredicate<Option<T>> some(final Class<T> aClass) {
        return some();
    }

    @Deprecated // will be removed after 950. Use flatMap instead.
    public static <T> LogicalPredicate<Option<T>> some() {
        return new LogicalPredicate<Option<T>>() {
            public final boolean matches(Option<T> other) {
                return !other.isEmpty();
            }
        };
    }

    public static LogicalPredicate<Class<?>> assignableTo(final Object o) {
        return new LogicalPredicate<Class<?>>() {
            public boolean matches(Class<?> aClass) {
                return isAssignableTo(o, aClass);
            }
        };
    }

    public static LogicalPredicate<Object> assignableTo(final Class<?> aClass) {
        return new LogicalPredicate<Object>() {
            public boolean matches(Object o) {
                return isAssignableTo(o, aClass);
            }
        };
    }

    public static boolean isAssignableTo(Object o, Class<?> aClass) {
        if (o == null) return false;
        return aClass.isAssignableFrom(o.getClass());
    }

    public static LogicalPredicate<Class<?>> classAssignableFrom(final Class<?> aClass) {
        return new LogicalPredicate<Class<?>>() {
            @Override
            public boolean matches(Class<?> other) {
                return other.isAssignableFrom(aClass);
            }
        };
    }

    public static LogicalPredicate<Class<?>> classAssignableTo(final Class<?> aClass) {
        return new LogicalPredicate<Class<?>>() {
            @Override
            public boolean matches(Class<?> other) {
                return aClass.isAssignableFrom(other);
            }
        };
    }

    public static <T, R> LogicalPredicate<T> where(final Callable1<? super T, ? extends R> callable, final Predicate<? super R> predicate) {
        return WherePredicate.where(callable, predicate);
    }

    public static <T, R> LogicalPredicate<T> by(final Callable1<? super T, ? extends R> callable, final Predicate<? super R> predicate) {
        return WherePredicate.where(callable, predicate);
    }

    public static <T> LogicalPredicate<Predicate<T>> matches(final T instance) {
        return new LogicalPredicate<Predicate<T>>() {
            @Override
            public boolean matches(Predicate<T> predicate) {
                return predicate.matches(instance);
            }
        };
    }

    public static <T extends Comparable<? super T>> LogicalPredicate<T> greaterThan(final T comparable) {
        return GreaterThanPredicate.greaterThan(comparable);
    }

    public static <T extends Comparable<? super T>> LogicalPredicate<T> greaterThanOrEqualTo(final T comparable) {
        return new GreaterThanOrEqualToPredicate<T>(comparable);
    }

    public static <T extends Comparable<? super T>> LogicalPredicate<T> lessThan(final T comparable) {
        return LessThanPredicate.lessThan(comparable);
    }

    public static <T extends Comparable<? super T>> LogicalPredicate<T> lessThanOrEqualTo(final T comparable) {
        return LessThanOrEqualToPredicate.lessThanOrEqualTo(comparable);
    }

    public static <T extends Comparable<? super T>> LogicalPredicate<T> between(final T lower, final T upper) {
        return new BetweenPredicate<T>(lower, upper);
    }

    public static Predicate<Pair> equalTo() {
        return new Predicate<Pair>() {
            public boolean matches(Pair pair) {
                return pair.first().equals(pair.second());
            }
        };
    }

    public static <T> LogicalPredicate<Iterable<T>> empty() {
        return new LogicalPredicate<Iterable<T>>() {
            public boolean matches(Iterable<T> other) {
                return sequence(other).isEmpty();
            }
        };
    }

    public static <T> LogicalPredicate<Iterable<T>> empty(Class<T> aClass) {
        return empty();
    }

    public static <T> LogicalPredicate<T> predicate(final Callable1<T, Boolean> callable) {
        return new LogicalPredicate<T>() {
            @Override
            public boolean matches(T other) {
                Boolean result = function(callable).apply(other);
                return result == null ? false : result;
            }
        };
    }

    public static <F> LogicalPredicate<First<F>> first(Predicate<? super F> predicate) {
        return where(Callables.<F>first(), predicate);
    }

    public static <S> LogicalPredicate<Second<S>> second(Predicate<? super S> predicate) {
        return where(Callables.<S>second(), predicate);
    }
}

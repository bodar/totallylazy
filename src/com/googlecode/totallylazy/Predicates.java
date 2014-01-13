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
import com.googlecode.totallylazy.predicates.Not;
import com.googlecode.totallylazy.predicates.NullPredicate;
import com.googlecode.totallylazy.predicates.OnlyOnce;
import com.googlecode.totallylazy.predicates.OrPredicate;
import com.googlecode.totallylazy.predicates.WherePredicate;
import com.googlecode.totallylazy.predicates.WhileTrue;

import java.util.Collection;

import static com.googlecode.totallylazy.Sequences.sequence;
import static com.googlecode.totallylazy.Sets.set;

public class Predicates {
    public static <T> Predicate<T> alwaysTrue() {
        return AlwaysTrue.alwaysTrue();
    }

    public static <T> Predicate<T> alwaysTrue(Class<T> aClass) {
        return AlwaysTrue.alwaysTrue();
    }

    public static <T> Predicate<T> always() {
        return AlwaysTrue.alwaysTrue();
    }

    public static <T> Predicate<T> always(Class<T> aClass) {
        return AlwaysTrue.alwaysTrue();
    }

    public static <T> Predicate<T> all() {
        return AlwaysTrue.alwaysTrue();
    }

    public static <T> Predicate<T> all(Class<T> aClass) {
        return AlwaysTrue.alwaysTrue();
    }

    public static <T> Predicate<T> any() {
        return AlwaysTrue.alwaysTrue();
    }

    public static <T> Predicate<T> any(Class<T> aClass) {
        return AlwaysTrue.alwaysTrue();
    }

    public static <T> Predicate<T> anything() {
        return AlwaysTrue.alwaysTrue();
    }

    public static <T> Predicate<T> anything(Class<T> aClass) {
        return AlwaysTrue.alwaysTrue();
    }

    public static <T> Predicate<T> alwaysFalse() {
        return AlwaysFalse.alwaysFalse();
    }

    public static <T> Predicate<T> alwaysFalse(Class<T> aClass) {
        return AlwaysFalse.alwaysFalse();
    }

    public static <T> Predicate<T> never() {
        return AlwaysFalse.alwaysFalse();
    }

    public static <T> Predicate<T> never(Class<T> aClass) {
        return AlwaysFalse.alwaysFalse();
    }

    public static <T> Predicate<Collection<T>> contains(final T t) {
        return new Predicate<Collection<T>>() {
            public boolean matches(Collection<T> other) {
                return other.contains(t);
            }
        };
    }

    public static <T> Predicate<Iterable<T>> exists(final Predicate<? super T> predicate) {
        return new Predicate<Iterable<T>>() {
            public boolean matches(Iterable<T> iterable) {
                return sequence(iterable).exists(predicate);
            }
        };
    }

    public static <T> Predicate<Iterable<T>> forAll(final Predicate<? super T> predicate) {
        return new Predicate<Iterable<T>>() {
            public boolean matches(Iterable<T> iterable) {
                return sequence(iterable).forAll(predicate);
            }
        };
    }

    public static <T> Predicate<Iterable<T>> subsetOf(final Iterable<? extends T> superset) {
        return forAll(in(superset));
    }

    public static <T> Predicate<Iterable<T>> supersetOf(final Iterable<? extends T> subset) {
        return new Predicate<Iterable<T>>() {
            public boolean matches(Iterable<T> superset) {
                return sequence(subset).forAll(in(superset));
            }
        };
    }

    public static <T> Predicate<Iterable<T>> setEqualityWith(final Iterable<? extends T> other) {
        return new Predicate<Iterable<T>>() {
            public boolean matches(Iterable<T> iterable) {
                return set(iterable).equals(set(other));
            }
        };
    }

    @SafeVarargs
    public static <T> Predicate<T> in(final T... values) {
        return in(sequence(values));
    }

    public static <T> Predicate<T> in(final Iterable<? extends T> values) {
        return InPredicate.in(values);
    }

    public static <T> Predicate<T> in(final Collection<? extends T> values) {
        return InPredicate.in(values);
    }

    public static Predicate<Either> isLeft() {
        return Either::isLeft;
    }

    public static Predicate<Either> isRight() {
        return Either::isRight;
    }

    public static <T> Predicate<T> onlyOnce(final Predicate<? super T> predicate) {
        return new OnlyOnce<>(predicate);
    }

    public static <T> Predicate<T> instanceOf(final Class<?> t) {
        return new InstanceOf<>(t);
    }

    public static <T> Predicate<T> equalTo(final T t) {
        return EqualsPredicate.equalTo(t);
    }

    public static <T> Predicate<T> is(final T t) {
        return equalTo(t);
    }

    public static <S, T extends Predicate<S>> T is(final T t) {
        return t;
    }

    public static <T> Predicate<T> and() {
        return AndPredicate.and(Sequences.<Predicate<T>>empty());
    }

    public static <T> Predicate<T> and(final Predicate<? super T> first) {
        return Predicate.predicate(first);
    }

    @SafeVarargs
    public static <T> Predicate<T> and(final Predicate<? super T>... predicates) {
        return and(sequence(predicates));
    }

    public static <T> Predicate<T> and(final Iterable<? extends Predicate<? super T>> predicates) {
        return AndPredicate.and(predicates);
    }

    public static <T> Predicate<T> or() {
        return OrPredicate.or(Sequences.<Predicate<T>>empty());
    }

    public static <T> Predicate<T> or(final Predicate<? super T> first) {
        return Predicate.predicate(first);
    }

    @SafeVarargs
    public static <T> Predicate<T> or(final Predicate<? super T>... predicates) {
        return or(sequence(predicates));
    }

    public static <T> Predicate<T> or(final Iterable<? extends Predicate<? super T>> predicates) {
        return OrPredicate.or(predicates);
    }

    public static <T> Predicate<T> not(final T t) {
        return not(is(t));
    }

    public static <T> Predicate<T> not(final Predicate<? super T> t) {
        return Not.not(t);
    }

    public static Predicate<Object> countTo(final Number count) {
        return new CountTo(count);
    }

    public static <T> Predicate<T> whileTrue(final Predicate<? super T> t) {
        return new WhileTrue<T>(t);
    }

    public static <T> Predicate<T> nullValue() {
        return new NullPredicate<T>();
    }

    public static <T> Predicate<T> nullValue(final Class<T> type) {
        return nullValue();
    }

    public static <T> Predicate<T> notNullValue() {
        return not(nullValue());
    }

    public static <T> Predicate<T> notNullValue(final Class<T> aClass) {
        return notNullValue();
    }

    @Deprecated // will be removed after 950. Use flatMap instead.
    public static <T> Predicate<Option<T>> some(final Class<T> aClass) {
        return some();
    }

    @Deprecated // will be removed after 950. Use flatMap instead.
    public static <T> Predicate<Option<T>> some() {
        return new Predicate<Option<T>>() {
            public final boolean matches(Option<T> other) {
                return !other.isEmpty();
            }
        };
    }

    public static Predicate<Class<?>> assignableTo(final Object o) {
        return aClass -> isAssignableTo(o, aClass);
    }

    public static Predicate<Object> assignableTo(final Class<?> aClass) {
        return o -> isAssignableTo(o, aClass);
    }

    public static boolean isAssignableTo(Object o, Class<?> aClass) {
        if (o == null) return false;
        return aClass.isAssignableFrom(o.getClass());
    }

    public static Predicate<Class<?>> classAssignableFrom(final Class<?> aClass) {
        return other -> other.isAssignableFrom(aClass);
    }

    public static Predicate<Class<?>> classAssignableTo(final Class<?> aClass) {
        return aClass::isAssignableFrom;
    }

    public static <T, R> Predicate<T> where(final Function<? super T, ? extends R> callable, final Predicate<? super R> predicate) {
        return WherePredicate.where(callable, predicate);
    }

    public static <T, R> Predicate<T> by(final Function<? super T, ? extends R> callable, final Predicate<? super R> predicate) {
        return WherePredicate.where(callable, predicate);
    }

    public static <T> Predicate<Predicate<T>> matches(final T instance) {
        return new Predicate<Predicate<T>>() {
            @Override
            public boolean matches(Predicate<T> predicate) {
                return predicate.matches(instance);
            }
        };
    }

    public static <T extends Comparable<? super T>> Predicate<T> greaterThan(final T comparable) {
        return GreaterThanPredicate.greaterThan(comparable);
    }

    public static <T extends Comparable<? super T>> Predicate<T> greaterThanOrEqualTo(final T comparable) {
        return new GreaterThanOrEqualToPredicate<T>(comparable);
    }

    public static <T extends Comparable<? super T>> Predicate<T> lessThan(final T comparable) {
        return LessThanPredicate.lessThan(comparable);
    }

    public static <T extends Comparable<? super T>> Predicate<T> lessThanOrEqualTo(final T comparable) {
        return LessThanOrEqualToPredicate.lessThanOrEqualTo(comparable);
    }

    public static <T extends Comparable<? super T>> Predicate<T> between(final T lower, final T upper) {
        return new BetweenPredicate<T>(lower, upper);
    }

    public static Predicate<Pair> equalTo() {
        return new Predicate<Pair>() {
            public boolean matches(Pair pair) {
                return pair.first().equals(pair.second());
            }
        };
    }

    public static <T> Predicate<Iterable<T>> empty() {
        return new Predicate<Iterable<T>>() {
            public boolean matches(Iterable<T> other) {
                return sequence(other).isEmpty();
            }
        };
    }

    public static <T> Predicate<Iterable<T>> empty(Class<T> aClass) {
        return empty();
    }

    public static <T> Predicate<T> predicate(final Function<T, Boolean> callable) {
        return new Predicate<T>() {
            @Override
            public boolean matches(T other) {
                Boolean result = callable.apply(other);
                return result == null ? false : result;
            }
        };
    }

    public static <F> Predicate<First<F>> first(Predicate<? super F> predicate) {
        return where(Callables.<F>first(), predicate);
    }

    public static <S> Predicate<Second<S>> second(Predicate<? super S> predicate) {
        return where(Callables.<S>second(), predicate);
    }
}

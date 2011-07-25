package com.googlecode.totallylazy;

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
import com.googlecode.totallylazy.predicates.NotNullPredicate;
import com.googlecode.totallylazy.predicates.NullPredicate;
import com.googlecode.totallylazy.predicates.OnlyOnce;
import com.googlecode.totallylazy.predicates.OrPredicate;
import com.googlecode.totallylazy.predicates.WherePredicate;
import com.googlecode.totallylazy.predicates.WhileTrue;

import java.util.Collection;

import static com.googlecode.totallylazy.Sequences.sequence;

public class Predicates {
    public static <T> LogicalPredicate<T> always(Class<T> aClass) {
        return always();
    }

    public static <T> LogicalPredicate<T> always() {
        return new LogicalPredicate<T>() {
            public boolean matches(T instance) {
                return true;
            }
        };
    }

    public static <T> LogicalPredicate<T> never(Class<T> aClass) {
        return never();
    }

    public static <T> LogicalPredicate<T> never() {
        return not(always());
    }

    public static <T> LogicalPredicate<Collection<? extends T>> contains(final T t) {
        return new LogicalPredicate<Collection<? extends T>>() {
            public boolean matches(Collection<? extends T> other) {
                return other.contains(t);
            }
        };
    }

    public static <T> LogicalPredicate<? super Iterable<T>> exists(final Predicate<? super T> predicate) {
        return new LogicalPredicate<Iterable<T>>() {
            public boolean matches(Iterable<T> iterable) {
                return sequence(iterable).exists(predicate);
            }
        };
    }

    public static <T> LogicalPredicate<T> in(final T... values) {
        return in(sequence(values));
    }

    public static <T> LogicalPredicate<T> in(final Sequence<T> values) {
        return new InPredicate<T>(values);
    }

    public static LogicalPredicate<? super Either> isLeft() {
        return new LogicalPredicate<Either>() {
            public boolean matches(Either either) {
                return either.isLeft();
            }
        };
    }

    public static LogicalPredicate<? super Either> isRight() {
        return new LogicalPredicate<Either>() {
            public boolean matches(Either either) {
                return either.isRight();
            }
        };
    }

    public static <T> LogicalPredicate<T> onlyOnce(final Predicate<? super T> predicate) {
        return new OnlyOnce<T>(predicate);
    }

    public static <T> LogicalPredicate<T> instanceOf(final Class t) {
        return new InstanceOf<T>(t);
    }

    public static <T> LogicalPredicate<T> equalTo(final T t) {
        return new EqualsPredicate<T>(t);
    }

    public static <T> LogicalPredicate<T> is(final T t) {
        return equalTo(t);
    }

    public static <S, T extends Predicate<S>> T is(final T t) {
        return t;
    }

    public static <T> LogicalPredicate<T> and(final Predicate<? super T>... predicates) {
        return new AndPredicate<T>(predicates);
    }

    public static <T> LogicalPredicate<T> or(final Predicate<? super T>... predicates) {
        return new OrPredicate<T>(predicates);
    }

    public static <T> LogicalPredicate<T> not(final T t) {
        return new Not<T>(is(t));
    }

    public static <T> LogicalPredicate<T> not(final Predicate<? super T> t) {
        return new Not<T>(t);
    }

    public static <T> LogicalPredicate<T> countTo(final int count) {
        return new CountTo<T>(count);
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
        return new NotNullPredicate<T>();
    }

    public static <T> LogicalPredicate<T> notNullValue(final Class<T> aClass) {
        return notNullValue();
    }

    public static <T> LogicalPredicate<Option<T>> some(final Class<T> aClass) {
        return some();
    }

    public static <T> LogicalPredicate<Option<T>> some() {
        return new LogicalPredicate<Option<T>>() {
            public final boolean matches(Option<T> other) {
                return !other.isEmpty();
            }
        };
    }

    public static LogicalPredicate<Class> assignableTo(final Object o) {
        return new LogicalPredicate<Class>() {
            public boolean matches(Class aClass) {
                return isAssignableTo(o, aClass);
            }
        };
    }

    public static LogicalPredicate<Object> assignableTo(final Class aClass) {
        return new LogicalPredicate<Object>() {
            public boolean matches(Object o) {
                return isAssignableTo(o, aClass);
            }
        };
    }

    public static boolean isAssignableTo(Object o, Class aClass) {
        if (o == null) return false;
        return aClass.isAssignableFrom(o.getClass());
    }

    public static <T, R> LogicalPredicate<T> where(final Callable1<? super T, R> callable, final Predicate<? super R> predicate) {
        return new WherePredicate<T, R>(callable, predicate);
    }

    public static <T, R> LogicalPredicate<T> by(final Callable1<? super T, R> callable, final Predicate<? super R> predicate) {
        return where(callable, predicate);
    }

    public static <T> LogicalPredicate<? super Predicate<? super T>> matches(final T instance) {
        return new LogicalPredicate<Predicate<? super T>>() {
            public boolean matches(Predicate<? super T> predicate) {
                return predicate.matches(instance);
            }
        };
    }

    public static <T extends Comparable<T>> LogicalPredicate<T> greaterThan(final T comparable) {
        return new GreaterThanPredicate<T>(comparable);
    }

    public static <T extends Comparable<T>> LogicalPredicate<T> greaterThanOrEqualTo(final T comparable) {
        return new GreaterThanOrEqualToPredicate<T>(comparable);
    }

    public static <T extends Comparable<T>> LogicalPredicate<T> lessThan(final T comparable) {
        return new LessThanPredicate<T>(comparable);
    }

    public static <T extends Comparable<T>> LogicalPredicate<T> lessThanOrEqualTo(final T comparable) {
        return new LessThanOrEqualToPredicate<T>(comparable);
    }

    public static <T extends Comparable<T>> LogicalPredicate<T> between(final T lower, final T upper) {
        return new BetweenPredicate<T>(lower, upper);
    }

}

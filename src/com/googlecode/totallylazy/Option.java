package com.googlecode.totallylazy;

import com.googlecode.totallylazy.functions.Function1;
import com.googlecode.totallylazy.functions.Function2;
import com.googlecode.totallylazy.functions.Functions;
import com.googlecode.totallylazy.functions.Unary;
import com.googlecode.totallylazy.predicates.Predicate;

import java.util.concurrent.Callable;

import static com.googlecode.totallylazy.Sequences.sequence;

public abstract class Option<A> implements Iterable<A>, Value<A>, Functor<A>, Applicative<A>, Monad<A>, Foldable<A> {
    public static <A> Option<A> option(A a) {
        if (a == null) {
            return None.none();
        }
        return Some.some(a);
    }

    public static <A> Option<A> option(Callable<? extends A> t) {
        try {
            return Option.option(t.call());
        } catch (Exception e) {
            return none();
        }
    }

    public static <A> Option<A> some(A a) {
        return Some.some(a);
    }

    public static <A> Option<A> none() {
        return None.none();
    }

    public static <A> Option<A> none(Class<A> aClass) {
        return None.none(aClass);
    }

    public static <T> Unary<Option<T>> identity(Class<T> aClass) {
        return identity();
    }

    public static <T> Unary<Option<T>> identity() {
        return Functions.identity();
    }

    public A value() {
        return get();
    }

    public abstract A get();

    public abstract boolean isEmpty();

    public boolean isDefined() { return !isEmpty(); }

    public abstract Option<A> orElse(Option<A> other);

    public abstract Option<A> orElse(Callable<? extends Option<A>> other);

    public abstract A getOrElse(A other);

    public abstract A getOrElse(Callable<? extends A> callable);

    public abstract A getOrNull();

    public abstract <E extends Exception> A getOrThrow(E e) throws E;

    public abstract <B> Option<B> map(Function1<? super A, ? extends B> callable);

    public abstract Option<A> each(Function1<? super A, ?> callable);

    public abstract <B> Option<B> flatMap(Function1<? super A, ? extends Option<? extends B>> callable);

    public abstract Option<A> filter(Predicate<? super A> predicate);

    public abstract <B> B fold(final B seed, final Function2<? super B, ? super A, ? extends B> callable);

    public abstract Sequence<A> join(final Iterable<? extends A> iterable);

    public Sequence<A> toSequence() {
        return sequence(this);
    }

    public abstract <L> Either<L, A> toEither(L value);

    public static <A> Option<A> flatten(Option<? extends Option<A>> option) {
        return option.flatMap(Functions.<Option<A>>identity());
    }

    public <B> Option<B> applicate(Option<? extends Function1<? super A, ? extends B>> applicator) {
        return applicate(applicator, this);
    }

    public static <A, B> Option<B> applicate(Option<? extends Function1<? super A, ? extends B>> applicator, Option<? extends A> option) {
        if (applicator.isEmpty()) return none();
        return option.map(applicator.get());
    }

    public static <A> Option<A> pure(A a) {
        return option(a);
    }

    public static <A> Function1<A, Option<A>> option() {
        return Option::option;
    }

    public <T> Option<T> unsafeCast() {
        return Unchecked.cast(this);
    }

    public <T> Option<T> unsafeCast(Class<T> aClass) {
        return unsafeCast();
    }

    public abstract boolean contains(A instance);

    public abstract boolean exists(Predicate<? super A> predicate);

    public boolean is(Predicate<? super A> predicate) { return exists(predicate);}

    public static class functions {
        public static <T> Function1<Option<T>, T> getOrElse(final T t) {
            return ts -> ts.getOrElse(t);
        }

        public static <T> Function1<Option<T>, T> getOrElse(final Callable<? extends T> callable) {
            return ts -> ts.getOrElse(callable);
        }

        public static <T> Function1<Option<T>, T> getOrNull() {
            return Option::getOrNull;
        }

        public static <T> Function1<Option<T>, T> get(Class<T> clazz) {
            return Option::get;
        }
    }
}
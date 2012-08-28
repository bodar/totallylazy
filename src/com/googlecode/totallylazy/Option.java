package com.googlecode.totallylazy;

import java.util.concurrent.Callable;

import static com.googlecode.totallylazy.Sequences.sequence;

public abstract class Option<A> implements Iterable<A>, Value<A>, Functor<A>, Applicative<A> {
    public static <A> Option<A> option(A a) {
        if (a == null) {
            return None.none();
        }
        return Some.some(a);
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

    public A value() {
        return get();
    }

    public abstract A get();

    public abstract boolean isEmpty();

    public abstract A getOrElse(A other);

    public abstract A getOrElse(Callable<? extends A> callable);

    public abstract A getOrNull();

    public abstract <B> Option<B> map(Callable1<? super A, ? extends B> callable);

    public abstract <B> Option<B> flatMap(Callable1<? super A, ? extends Option<B>> callable);

    public abstract <B> B fold(final B seed, final Callable2<? super B, ? super A, ? extends B> callable);

    public Sequence<A> toSequence() {
        return sequence(this);
    }

    public static <A> Option<A> flatten(Option<? extends Option<A>> option) {
        return option.flatMap(Functions.<Option<A>>identity());
    }

    public <B> Option<B> applicate(Option<? extends Callable1<? super A, ? extends B>> applicator) {
        return applicate(applicator, this);
    }

    public static <A, B> Option<B> applicate(Option<? extends Callable1<? super A, ? extends B>> applicator, Option<? extends A> option) {
        if (applicator.isEmpty()) return none();
        return option.map(applicator.get());
    }

    public static <A> Option<A> pure(A a){
        return option(a);
    }

    public static <A> Function1<A, Option<A>> option() {
        return new Function1<A, Option<A>>() {
            @Override
            public Option<A> call(A a) throws Exception {
                return option(a);
            }
        };
    }
}
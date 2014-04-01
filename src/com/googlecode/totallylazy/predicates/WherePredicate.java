package com.googlecode.totallylazy.predicates;

import com.googlecode.totallylazy.Function;
import com.googlecode.totallylazy.Function2;
import com.googlecode.totallylazy.Callers;
import com.googlecode.totallylazy.Functions;
import com.googlecode.totallylazy.Predicate;
import com.googlecode.totallylazy.Predicates;
import com.googlecode.totallylazy.Unchecked;

import static com.googlecode.totallylazy.Unchecked.cast;
import static java.lang.String.format;

public class WherePredicate<T, R> extends LogicalPredicate<T> {
    private final Function<? super T, ? extends R> callable;
    private final Predicate<? super R> predicate;

    private WherePredicate(final Function<? super T, ? extends R> callable, final Predicate<? super R> predicate) {
        this.predicate = predicate;
        this.callable = callable;
    }

    public static <T, R> LogicalPredicate<T> where(final Function<? super T, ? extends R> callable, final Predicate<? super R> predicate) {
        if(predicate instanceof AlwaysTrue) return Predicates.alwaysTrue();
        if(predicate instanceof AlwaysFalse) return Predicates.alwaysFalse();
        if(predicate instanceof Not) return where(callable, Unchecked.<Not<R>>cast(predicate).predicate()).not();
        return new WherePredicate<>(callable, predicate);
    }

    public static <T, R> Function<T, Predicate<T>> asWhere(final Function2<? super T, ? super T, ? extends R> callable, final Predicate<? super R> predicate) {
        return t -> where(callable.apply(t), predicate);
    }

    public boolean matches(T o) {
        return predicate.matches(Callers.call(callable, o));
    }

    public Function<T, R> callable() {
        return cast(callable);
    }

    public Predicate<R> predicate() {
        return cast(predicate);
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof WherePredicate && callable.equals(((WherePredicate) obj).callable()) && predicate.equals(((WherePredicate) obj).predicate());
    }

    @Override
    public int hashCode() {
        return 19 * callable.hashCode() * predicate.hashCode();
    }

    @Override
    public String toString() {
        return format("where %s is %s", callable, predicate);
    }
}

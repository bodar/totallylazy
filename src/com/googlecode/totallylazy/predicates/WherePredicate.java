package com.googlecode.totallylazy.predicates;

import com.googlecode.totallylazy.*;

import static com.googlecode.totallylazy.Unchecked.cast;
import static java.lang.String.format;

public class WherePredicate<T, R> extends LogicalPredicate<T> {
    private final Function1<? super T, ? extends R> callable;
    private final Predicate<? super R> predicate;

    private WherePredicate(final Function1<? super T, ? extends R> callable, final Predicate<? super R> predicate) {
        this.predicate = predicate;
        this.callable = callable;
    }

    public static <T, R> LogicalPredicate<T> where(final Function1<? super T, ? extends R> callable, final Predicate<? super R> predicate) {
        if(predicate instanceof AlwaysTrue) return Predicates.alwaysTrue();
        if(predicate instanceof AlwaysFalse) return Predicates.alwaysFalse();
        if(predicate instanceof Not) return Predicates.not(where(callable, Unchecked.<Not< ? super R >>cast(predicate).predicate()));
        return new WherePredicate<T, R>(callable, predicate);
    }

    public static <T, R> Function1<T, Predicate<T>> asWhere(final Function2<? super T, ? super T, ? extends R> callable, final Predicate<? super R> predicate) {
        return t -> where(((Function2<T, T, R>) callable).apply(t), predicate);
    }

    public boolean matches(T o) {
        return predicate.matches(Callers.call(callable, o));
    }

    public Function1<T, R> callable() {
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

package com.googlecode.totallylazy.predicates;

import com.googlecode.totallylazy.Callable1;
import com.googlecode.totallylazy.Callable2;
import com.googlecode.totallylazy.Callers;
import com.googlecode.totallylazy.Function1;
import com.googlecode.totallylazy.Functions;
import com.googlecode.totallylazy.Predicate;

import static com.googlecode.totallylazy.Unchecked.cast;
import static java.lang.String.format;

public class WherePredicate<T, R> extends LogicalPredicate<T> {
    private final Callable1<? super T, ? extends R> callable;
    private final Predicate<? super R> predicate;

    private WherePredicate(final Callable1<? super T, ? extends R> callable, final Predicate<? super R> predicate) {
        this.predicate = predicate;
        this.callable = callable;
    }

    public static <T, R> WherePredicate<T, R> where(final Callable1<? super T, ? extends R> callable, final Predicate<? super R> predicate) {
        return new WherePredicate<T, R>(callable, predicate);
    }

    public static <T, R> Function1<T, Predicate<T>> asWhere(final Callable2<? super T, ? super T, ? extends R> callable, final Predicate<? super R> predicate) {
        return new Function1<T, Predicate<T>>() {
            @Override
            public Predicate<T> call(T t) throws Exception {
                return where(Functions.function(callable).apply(t), predicate);
            }
        };
    }

    public boolean matches(T o) {
        return predicate.matches(Callers.call(callable, o));
    }

    public Callable1<T, R> callable() {
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

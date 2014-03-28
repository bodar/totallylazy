package com.googlecode.totallylazy.annotations;

import com.googlecode.totallylazy.Returns;
import com.googlecode.totallylazy.Function2;
import com.googlecode.totallylazy.Function;
import com.googlecode.totallylazy.predicates.LogicalPredicate;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Used by <a href="https://code.google.com/p/jcompilo/">jcompilo</a> to enable lambda processing
 */
@Retention(RetentionPolicy.CLASS)
@Target(ElementType.METHOD)
public @interface lambda {
    class functions {
        public static <T> Returns<T> λ(T result) {
            throw new IllegalStateException("JCompilo required");
        }

        public static <I, R> Function<I, R> λ(I input, R result) {
            throw new IllegalStateException("JCompilo required");
        }

        public static <F, S, R> Function2<F, S, R> λ(F first, S second, R result) {
            throw new IllegalStateException("JCompilo required");
        }

        public static <T> LogicalPredicate<T> λ(T input, boolean to) {
            throw new IllegalStateException("JCompilo required");
        }

        public static <T> Returns<T> lambda(T result) {
            throw new IllegalStateException("JCompilo required");
        }

        public static <I, R> Function<I, R> lambda(I input, R result) {
            throw new IllegalStateException("JCompilo required");
        }

        public static <F, S, R> Function2<F, S, R> lambda(F first, S second, R result) {
            throw new IllegalStateException("JCompilo required");
        }

        public static <T> LogicalPredicate<T> predicate(T input, boolean to) {
            throw new IllegalStateException("JCompilo required");
        }
    }
}

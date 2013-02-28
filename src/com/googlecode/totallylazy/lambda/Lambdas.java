package com.googlecode.totallylazy.lambda;

import com.googlecode.totallylazy.Function;
import com.googlecode.totallylazy.Function1;
import com.googlecode.totallylazy.Function2;
import com.googlecode.totallylazy.Predicate;
import com.googlecode.totallylazy.predicates.LogicalPredicate;
import org.enumerable.lambda.annotation.NewLambda;
import org.enumerable.lambda.exception.LambdaWeavingNotEnabledException;


public class Lambdas {
    @org.enumerable.lambda.annotation.LambdaParameter
    public static Number n;

    @NewLambda
    public static <T> Function<T> λ(T result) {
        throw new LambdaWeavingNotEnabledException();
    }

    @NewLambda
    public static <I, R> Function1<I, R> λ(I input, R result) {
        throw new LambdaWeavingNotEnabledException();
    }

    @NewLambda
    public static <F, S, R> Function2<F, S, R> λ(F first, S second, R result) {
        throw new LambdaWeavingNotEnabledException();
    }

    @NewLambda
    public static <T> Function<T> lambda(T result) {
        throw new LambdaWeavingNotEnabledException();
    }

    @NewLambda
    public static <I, R> Function1<I, R> lambda(I input, R result) {
        throw new LambdaWeavingNotEnabledException();
    }

    @NewLambda
    public static <F, S, R> Function2<F, S, R> lambda(F first, S second, R result) {
        throw new LambdaWeavingNotEnabledException();
    }

    @NewLambda
    public static <T> Predicate<T> λp(T input, boolean to) {
        throw new LambdaWeavingNotEnabledException();
    }

    @NewLambda
    public static <T> Predicate<T> predicate(T input, boolean to) {
        throw new LambdaWeavingNotEnabledException();
    }
}

package com.googlecode.totallylazy.lambda;

import com.googlecode.totallylazy.Callable1;
import com.googlecode.totallylazy.Callable2;
import com.googlecode.totallylazy.Predicate;
import lambda.annotation.NewLambda;
import lambda.exception.LambdaWeavingNotEnabledException;

import java.util.concurrent.Callable;

public class Lambdas {
    @NewLambda
    public static <T> Callable<T> λ(T result) {
        throw new LambdaWeavingNotEnabledException();
    }

    @NewLambda
    public static <I, R> Callable1<I, R> λ(I input, R result) {
        throw new LambdaWeavingNotEnabledException();
    }

    @NewLambda
    public static <F, S, R> Callable2<F, S, R> λ(F first, S second, R result) {
        throw new LambdaWeavingNotEnabledException();
    }

    @NewLambda
    public static <T> Callable<T> lambda(T result) {
        throw new LambdaWeavingNotEnabledException();
    }

    @NewLambda
    public static <I, R> Callable1<I, R> lambda(I input, R result) {
        throw new LambdaWeavingNotEnabledException();
    }

    @NewLambda
    public static <F, S, R> Callable2<F, S, R> lambda(F first, S second, R result) {
        throw new LambdaWeavingNotEnabledException();
    }

    @NewLambda
    public static <T> Predicate<T> λ(T input, boolean to) {
        throw new LambdaWeavingNotEnabledException();
    }

    @NewLambda
    public static <T> Predicate<T> predicate(T input, boolean to) {
        throw new LambdaWeavingNotEnabledException();
    }
}

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
    public static <F, T> Callable1<F, T> λ(F from, T to) {
        throw new LambdaWeavingNotEnabledException();
    }

    @NewLambda
    public static <F, S, R> Callable2<F, S, R> λ(F first, S second, R result) {
        throw new LambdaWeavingNotEnabledException();
    }

    @NewLambda
    public static <T> Predicate<T> λ(T input, boolean to) {
        throw new LambdaWeavingNotEnabledException();
    }
}

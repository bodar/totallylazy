package com.googlecode.totallylazy.functions;

public interface CurriedBinaryFunction<T> extends CurriedFunction2<T, T, T>, BinaryFunction<T> {
    @Override
    default UnaryFunction<T> apply(T a) {
        return b -> call(a, b);
    }

    @Override
    default CurriedBinaryFunction<T> flip() {
        return (a, b) -> call(b,a);
    }
}

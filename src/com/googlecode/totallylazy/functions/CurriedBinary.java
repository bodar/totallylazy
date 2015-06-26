package com.googlecode.totallylazy.functions;

public interface CurriedBinary<T> extends Curried2<T, T, T>, Binary<T> {
    @Override
    default Unary<T> apply(T a) {
        return b -> call(a, b);
    }

    @Override
    default CurriedBinary<T> flip() {
        return (a, b) -> call(b,a);
    }
}

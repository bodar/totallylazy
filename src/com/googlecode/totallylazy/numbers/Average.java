package com.googlecode.totallylazy.numbers;

import com.googlecode.totallylazy.Callable2;

import static com.googlecode.totallylazy.numbers.Numbers.*;

public class Average<T extends Number> implements Callable2<T, T, Number> {
    private Number count = 1;

    public Number call(T previousAverage, T value) {
        count = increment(count);
        return add(divide(subtract(value, previousAverage), count), previousAverage);
    }
}

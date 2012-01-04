package com.googlecode.totallylazy.numbers;

import com.googlecode.totallylazy.Callable2;
import com.googlecode.totallylazy.Function2;

import static com.googlecode.totallylazy.numbers.Numbers.add;
import static com.googlecode.totallylazy.numbers.Numbers.divide;
import static com.googlecode.totallylazy.numbers.Numbers.increment;
import static com.googlecode.totallylazy.numbers.Numbers.subtract;

public class Average<T extends Number> extends Function2<T, T, Number> {
    private Number count = 1;

    public Number call(T previousAverage, T value) {
        count = increment(count);
        return add(divide(subtract(value, previousAverage), count), previousAverage);
    }
}

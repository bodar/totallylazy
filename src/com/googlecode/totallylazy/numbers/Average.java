package com.googlecode.totallylazy.numbers;

import com.googlecode.totallylazy.Function2;
import com.googlecode.totallylazy.Identity;

import static com.googlecode.totallylazy.numbers.Numbers.add;
import static com.googlecode.totallylazy.numbers.Numbers.divide;
import static com.googlecode.totallylazy.numbers.Numbers.increment;
import static com.googlecode.totallylazy.numbers.Numbers.subtract;

public class Average extends Function2<Number, Number, Number> implements Identity<Number> {
    private Number count = 0;

    public Number call(Number previousAverage, Number value) {
        count = increment(count);
        return add(divide(subtract(value, previousAverage), count), previousAverage);
    }

    @Override
    public Number identity() {
        return 0;
    }
}

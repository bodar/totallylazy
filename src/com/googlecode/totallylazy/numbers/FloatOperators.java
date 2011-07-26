/*
This code is a a heavily modified version of Numbers from Rich Hickeys clojure core, see Numbers.java for copyright
 */

package com.googlecode.totallylazy.numbers;

import java.math.BigDecimal;

public final class FloatOperators implements Operators<Float> {
    public static FloatOperators Instance = new FloatOperators();

    private FloatOperators() {}

    public final Class<Float> forClass() {
        return Float.class;
    }

    public final int priority() {
        return 6;
    }

    public final Number negate(Float value) {
        return -value;
    }

    public final Number increment(Float value) {
        return value + 1;
    }

    public final Number decrement(Float value) {
        return value - 1;
    }

    public final boolean isZero(Float value) {
        return value == 0;
    }

    public final boolean isPositive(Float value) {
        return value > 0;
    }

    public final boolean isNegative(Float value) {
        return value < 0;
    }

    public final boolean equalTo(Number x, Number y) {
        return x.floatValue() == y.floatValue();
    }

    public final boolean lessThan(Number x, Number y) {
        return x.floatValue() < y.floatValue();
    }

    public final Number add(Number x, Number y) {
        return x.floatValue() + y.floatValue();
    }

    public final Number multiply(Number x, Number y) {
        return x.floatValue() * y.floatValue();
    }

    public final Number divide(Number x, Number y) {
        return x.floatValue() / y.floatValue();
    }

    public final Number quotient(Number x, Number y) {
        return DoubleOperators.quotient(x.doubleValue(), y.doubleValue());
    }

    public final Number remainder(Number x, Number y) {
        return DoubleOperators.remainder(x.doubleValue(), y.doubleValue());
    }

    public static Number rationalize(Float x) {
        return BigDecimalOperators.rationalize(BigDecimal.valueOf(x));
    }

}

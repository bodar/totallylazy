/*
This code is a a heavily modified version of Numbers from Rich Hickeys clojure core, see Numbers.java for copyright
 */

package com.googlecode.totallylazy.numbers;

import java.math.BigDecimal;

public final class DoubleOperators implements Operators<Double> {
    public static DoubleOperators Instance = new DoubleOperators();

    private DoubleOperators() {}

    public final Class<Double> forClass() {
        return Double.class;
    }

    public final int priority() {
        return 7;
    }

    @Override
    public Number absolute(Double value) {
        return Math.abs(value);
    }

    public final Number negate(Double value) {
        return -value;
    }

    public final Number increment(Double value) {
        return value + 1;
    }

    public final Number decrement(Double value) {
        return value - 1;
    }

    public final boolean isZero(Double value) {
        return value == 0;
    }

    public final boolean isPositive(Double value) {
        return value > 0;
    }

    public final boolean isNegative(Double value) {
        return value < 0;
    }

    public final boolean equalTo(Number x, Number y) {
        return x.doubleValue() == y.doubleValue();
    }

    public final boolean lessThan(Number x, Number y) {
        return x.doubleValue() < y.doubleValue();
    }

    public final Number add(Number x, Number y) {
        return x.doubleValue() + y.doubleValue();
    }

    public final Number multiply(Number x, Number y) {
        return x.doubleValue() * y.doubleValue();
    }

    public final Number divide(Number x, Number y) {
        return x.doubleValue() / y.doubleValue();
    }

    public final Number quotient(Number x, Number y) {
        return quotient(x.doubleValue(), y.doubleValue());
    }


    public final Number remainder(Number x, Number y) {
        return remainder(x.doubleValue(), y.doubleValue());
    }

    public static Number quotient(double n, double d) {
        double q = n / d;
        if (q <= Integer.MAX_VALUE && q >= Integer.MIN_VALUE) {
            return (int) q;
        } else {
            return BigIntegerOperators.reduce(new BigDecimal(q).toBigInteger());
        }
    }

    public static Number remainder(double n, double d) {
        double q = n / d;
        if (q <= Integer.MAX_VALUE && q >= Integer.MIN_VALUE) {
            return (n - ((int) q) * d);
        } else {
            Number bq = BigIntegerOperators.reduce(new BigDecimal(q).toBigInteger());
            return (n - bq.doubleValue() * d);
        }
    }

    public static Number rationalize(Double x) {
        return BigDecimalOperators.rationalize(BigDecimal.valueOf(x));
    }

}

/*
This code is a a heavily modified version of Numbers from Rich Hickeys clojure core, see Numbers.java for copyright
 */

package com.googlecode.totallylazy.numbers;

import java.math.BigDecimal;
import java.math.BigInteger;

import static java.math.BigDecimal.ONE;

public final class BigDecimalOperators implements Operators<BigDecimal> {
    public static BigDecimalOperators Instance = new BigDecimalOperators();

    private BigDecimalOperators() {}

    public final Class<BigDecimal> forClass() {
        return BigDecimal.class;
    }

    public final int priority() {
        return 4;
    }

    public final Number negate(BigDecimal value) {
        return value.negate();
    }

    public final Number increment(BigDecimal value) {
        return value.add(ONE);
    }

    public final Number decrement(BigDecimal value) {
        return value.subtract(ONE);
    }

    public final boolean isZero(BigDecimal value) {
        return value.signum() == 0;
    }

    public final boolean isPositive(BigDecimal value) {
        return value.signum() > 0;
    }

    public final boolean isNegative(BigDecimal value) {
        return value.signum() < 0;
    }

    public final boolean equalTo(Number x, Number y) {
        return x.equals(toBigDecimal(y));
    }

    public final boolean lessThan(Number x, Number y) {
        return toBigDecimal(x).compareTo(toBigDecimal(y)) < 0;
    }

    public final Number add(Number x, Number y) {
        return toBigDecimal(x).add(toBigDecimal(y));
    }

    public final Number multiply(Number x, Number y) {
        return toBigDecimal(x).multiply(toBigDecimal(y));
    }

    public final Number divide(Number x, Number y) {
        return toBigDecimal(x).divide(toBigDecimal(y));
    }

    public final Number quotient(Number x, Number y) {
        return toBigDecimal(x).divideToIntegralValue(toBigDecimal(y));
    }

    public final Number remainder(Number x, Number y) {
        return toBigDecimal(x).remainder(toBigDecimal(y));
    }

    public static BigDecimal toBigDecimal(Number number) {
        if (number instanceof BigDecimal)
            return (BigDecimal) number;
        else if (number instanceof BigInteger)
            return new BigDecimal((BigInteger) number);
        else
            return BigDecimal.valueOf((number).longValue());
    }

    public static Number rationalize(BigDecimal number) {
        BigInteger unscaled = number.unscaledValue();
        int scale = number.scale();
        if (scale < 0) {
            return unscaled.multiply(BigInteger.TEN.pow(-scale));
        }
        else {
            return unscaled.divide(BigInteger.TEN.pow(scale));
        }
    }
}

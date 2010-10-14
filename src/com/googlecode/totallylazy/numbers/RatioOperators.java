/*
This code is a a heavily modified version of Numbers from Rich Hickeys clojure core, see Numbers.java for copyright
 */

package com.googlecode.totallylazy.numbers;

import java.math.BigDecimal;
import java.math.BigInteger;

public final class RatioOperators implements Operators<Ratio> {
    public Class<Ratio> forClass() {
        return Ratio.class;
    }

    public final Number negate(Ratio value) {
        return new Ratio(value.numerator.negate(), value.denominator);
    }

    public final Number increment(Ratio value) {
        return Numbers.add(value, 1);
    }

    public final Number decrement(Ratio value) {
        return Numbers.add(value, -1);
    }

    public final boolean isZero(Ratio value) {
        return value.numerator.signum() == 0;
    }

    public final boolean isPositive(Ratio value) {
        return value.numerator.signum() > 0;
    }

    public final boolean isNegative(Ratio value) {
        return value.numerator.signum() < 0;
    }

    public final boolean equalTo(Number x, Number y) {
        Ratio rx = toRatio(x);
        Ratio ry = toRatio(y);
        return rx.numerator.equals(ry.numerator)
                && rx.denominator.equals(ry.denominator);
    }

    public final boolean lessThan(Number x, Number y) {
        Ratio rx = toRatio(x);
        Ratio ry = toRatio(y);
        return Numbers.lessThan(rx.numerator.multiply(ry.denominator), ry.numerator.multiply(rx.denominator));
    }

    public final Number add(Number x, Number y) {
        Ratio rx = toRatio(x);
        Ratio ry = toRatio(y);
        return divide(ry.numerator.multiply(rx.denominator)
                .add(rx.numerator.multiply(ry.denominator))
                , ry.denominator.multiply(rx.denominator));
    }

    public final Number multiply(Number x, Number y) {
        Ratio rx = toRatio(x);
        Ratio ry = toRatio(y);
        return Numbers.divide(ry.numerator.multiply(rx.numerator)
                , ry.denominator.multiply(rx.denominator));
    }

    public final Number divide(Number x, Number y) {
        Ratio rx = toRatio(x);
        Ratio ry = toRatio(y);
        return Numbers.divide(ry.denominator.multiply(rx.numerator)
                , ry.numerator.multiply(rx.denominator));
    }

    public final Number quotient(Number x, Number y) {
        Ratio rx = toRatio(x);
        Ratio ry = toRatio(y);
        BigInteger q = rx.numerator.multiply(ry.denominator).divide(
                rx.denominator.multiply(ry.numerator));
        return Numbers.reduce(q);
    }

    public final Number remainder(Number x, Number y) {
        Ratio rx = toRatio(x);
        Ratio ry = toRatio(y);
        BigInteger q = rx.numerator.multiply(ry.denominator).divide(
                rx.denominator.multiply(ry.numerator));
        return Numbers.subtract(x, Numbers.multiply(q, y));
    }

    public static Ratio toRatio(Number value) {
        if (value instanceof Ratio)
            return (Ratio) value;
        else if (value instanceof BigDecimal) {
            BigDecimal bx = (BigDecimal) value;
            BigInteger bv = bx.unscaledValue();
            int scale = bx.scale();
            if (scale < 0)
                return new Ratio(bv.multiply(BigInteger.TEN.pow(-scale)), BigInteger.ONE);
            else
                return new Ratio(bv, BigInteger.TEN.pow(scale));
        }
        return new Ratio(BigIntegerOperators.bigInteger(value), BigInteger.ONE);
    }

}

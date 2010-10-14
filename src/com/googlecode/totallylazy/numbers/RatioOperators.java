package com.googlecode.totallylazy.numbers;
/**
 *   Copyright (c) Rich Hickey. All rights reserved.
 *   The use and distribution terms for this software are covered by the
 *   Eclipse Public License 1.0 (http://opensource.org/licenses/eclipse-1.0.php)
 *   which can be found in the file epl-v10.html at the root of this distribution.
 *   By using this software in any fashion, you are agreeing to be bound by
 * 	 the terms of this license.
 *   You must not remove this notice, or any other, from this software.
 **/

/* rich Mar 31, 2008 */

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
        return new Ratio(BigIntegerOperators.toBigInteger(value), BigInteger.ONE);
    }

}

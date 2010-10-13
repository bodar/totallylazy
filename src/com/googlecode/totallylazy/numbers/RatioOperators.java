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

import java.math.BigInteger;

public final class RatioOperators implements Operators<Ratio> {
    public final Operators combine(Operators y) {
        return y.opsWith(this);
    }

    public final Operators opsWith(IntegerOperators x) {
        return this;
    }

    public final Operators opsWith(LongOperators x) {
        return this;
    }

    public final Operators opsWith(FloatOperators x) {
        return Numbers.FLOAT_OPS;
    }

    public final Operators opsWith(DoubleOperators x) {
        return Numbers.DOUBLE_OPS;
    }

    public final Operators opsWith(RatioOperators x) {
        return this;
    }

    public final Operators opsWith(BigIntegerOperators x) {
        return this;
    }

    public final Operators opsWith(BigDecimalOperators x) {
        return this;
    }

    public final Number negate(Ratio x) {
        return new Ratio(x.numerator.negate(), x.denominator);
    }

    public final Number increment(Ratio x) {
        return Numbers.add(x, 1);
    }

    public final Number decrement(Ratio x) {
        return Numbers.add(x, -1);
    }

    public final boolean isZero(Ratio x) {
        return x.numerator.signum() == 0;
    }

    public final boolean isPositive(Ratio x) {
        return x.numerator.signum() > 0;
    }

    public final boolean isNegative(Ratio x) {
        return x.numerator.signum() < 0;
    }

    public final boolean equalTo(Number x, Number y) {
        Ratio rx = Numbers.toRatio(x);
        Ratio ry = Numbers.toRatio(y);
        return rx.numerator.equals(ry.numerator)
                && rx.denominator.equals(ry.denominator);
    }

    public final boolean lessThan(Number x, Number y) {
        Ratio rx = Numbers.toRatio(x);
        Ratio ry = Numbers.toRatio(y);
        return Numbers.lessThan(rx.numerator.multiply(ry.denominator), ry.numerator.multiply(rx.denominator));
    }

    public final Number add(Number x, Number y) {
        Ratio rx = Numbers.toRatio(x);
        Ratio ry = Numbers.toRatio(y);
        return divide(ry.numerator.multiply(rx.denominator)
                .add(rx.numerator.multiply(ry.denominator))
                , ry.denominator.multiply(rx.denominator));
    }

    public final Number multiply(Number x, Number y) {
        Ratio rx = Numbers.toRatio(x);
        Ratio ry = Numbers.toRatio(y);
        return Numbers.divide(ry.numerator.multiply(rx.numerator)
                , ry.denominator.multiply(rx.denominator));
    }

    public final Number divide(Number x, Number y) {
        Ratio rx = Numbers.toRatio(x);
        Ratio ry = Numbers.toRatio(y);
        return Numbers.divide(ry.denominator.multiply(rx.numerator)
                , ry.numerator.multiply(rx.denominator));
    }

    public final Number quotient(Number x, Number y) {
        Ratio rx = Numbers.toRatio(x);
        Ratio ry = Numbers.toRatio(y);
        BigInteger q = rx.numerator.multiply(ry.denominator).divide(
                rx.denominator.multiply(ry.numerator));
        return Numbers.reduce(q);
    }

    public final Number remainder(Number x, Number y) {
        Ratio rx = Numbers.toRatio(x);
        Ratio ry = Numbers.toRatio(y);
        BigInteger q = rx.numerator.multiply(ry.denominator).divide(
                rx.denominator.multiply(ry.numerator));
        return Numbers.subtract(x, Numbers.multiply(q, y));
    }

}

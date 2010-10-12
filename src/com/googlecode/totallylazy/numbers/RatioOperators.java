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

final class RatioOperators implements Operators {
    public Operators combine(Operators y) {
        return y.opsWith(this);
    }

    final public Operators opsWith(IntegerOperators x) {
        return this;
    }

    final public Operators opsWith(LongOperators x) {
        return this;
    }

    final public Operators opsWith(FloatOperators x) {
        return Numbers.FLOAT_OPS;
    }

    final public Operators opsWith(DoubleOperators x) {
        return Numbers.DOUBLE_OPS;
    }

    final public Operators opsWith(RatioOperators x) {
        return this;
    }

    final public Operators opsWith(BigIntegerOperators x) {
        return this;
    }

    final public Operators opsWith(BigDecimalOperators x) {
        return this;
    }

    public boolean isZero(Number x) {
        Ratio r = (Ratio) x;
        return r.numerator.signum() == 0;
    }

    public boolean isPositive(Number x) {
        Ratio r = (Ratio) x;
        return r.numerator.signum() > 0;
    }

    public boolean isNegative(Number x) {
        Ratio r = (Ratio) x;
        return r.numerator.signum() < 0;
    }

    final public Number add(Number x, Number y) {
        Ratio rx = Numbers.toRatio(x);
        Ratio ry = Numbers.toRatio(y);
        return divide(ry.numerator.multiply(rx.denominator)
                .add(rx.numerator.multiply(ry.denominator))
                , ry.denominator.multiply(rx.denominator));
    }

    final public Number multiply(Number x, Number y) {
        Ratio rx = Numbers.toRatio(x);
        Ratio ry = Numbers.toRatio(y);
        return Numbers.divide(ry.numerator.multiply(rx.numerator)
                , ry.denominator.multiply(rx.denominator));
    }

    public Number divide(Number x, Number y) {
        Ratio rx = Numbers.toRatio(x);
        Ratio ry = Numbers.toRatio(y);
        return Numbers.divide(ry.denominator.multiply(rx.numerator)
                , ry.numerator.multiply(rx.denominator));
    }

    public Number quotient(Number x, Number y) {
        Ratio rx = Numbers.toRatio(x);
        Ratio ry = Numbers.toRatio(y);
        BigInteger q = rx.numerator.multiply(ry.denominator).divide(
                rx.denominator.multiply(ry.numerator));
        return Numbers.reduce(q);
    }

    public Number remainder(Number x, Number y) {
        Ratio rx = Numbers.toRatio(x);
        Ratio ry = Numbers.toRatio(y);
        BigInteger q = rx.numerator.multiply(ry.denominator).divide(
                rx.denominator.multiply(ry.numerator));
        return Numbers.subtract(x, Numbers.multiply(q, y));
    }

    public boolean equalTo(Number x, Number y) {
        Ratio rx = Numbers.toRatio(x);
        Ratio ry = Numbers.toRatio(y);
        return rx.numerator.equals(ry.numerator)
                && rx.denominator.equals(ry.denominator);
    }

    public boolean lessThan(Number x, Number y) {
        Ratio rx = Numbers.toRatio(x);
        Ratio ry = Numbers.toRatio(y);
        return Numbers.lessThan(rx.numerator.multiply(ry.denominator), ry.numerator.multiply(rx.denominator));
    }

    //public Number subtract(Number x, Number y);
    final public Number negate(Number x) {
        Ratio r = (Ratio) x;
        return new Ratio(r.numerator.negate(), r.denominator);
    }

    public Number increment(Number x) {
        return Numbers.add(x, 1);
    }

    public Number decrement(Number x) {
        return Numbers.add(x, -1);
    }

}

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

import static com.googlecode.totallylazy.numbers.Numbers.*;
import static com.googlecode.totallylazy.numbers.Numbers.toBigDecimal;
import static java.math.BigDecimal.ONE;
import static java.math.MathContext.UNLIMITED;

public final class BigDecimalOperators implements Operators<BigDecimal> {

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
        return FLOAT_OPS;
    }

    public final Operators opsWith(DoubleOperators x) {
        return DOUBLE_OPS;
    }

    public final Operators opsWith(RatioOperators x) {
        return RATIO_OPS;
    }

    public final Operators opsWith(BigIntegerOperators x) {
        return this;
    }

    public final Operators opsWith(BigDecimalOperators x) {
        return this;
    }

    public final boolean isZero(BigDecimal x) {
        return toBigDecimal(x).signum() == 0;
    }

    public final boolean isPositive(BigDecimal x) {
        return toBigDecimal(x).signum() > 0;
    }

    public final boolean isNegative(BigDecimal x) {
        return toBigDecimal(x).signum() < 0;
    }

    public final Number add(Number x, Number y) {
        return toBigDecimal(x).add(toBigDecimal(y), UNLIMITED);
    }

    public final Number multiply(Number x, Number y) {
        return toBigDecimal(x).multiply(toBigDecimal(y), UNLIMITED);
    }

    public final Number divide(Number x, Number y) {
        return toBigDecimal(x).divide(toBigDecimal(y), UNLIMITED);
    }

    public final Number quotient(Number x, Number y) {
        return toBigDecimal(x).divideToIntegralValue(toBigDecimal(y), UNLIMITED);
    }

    public final Number remainder(Number x, Number y) {
        return toBigDecimal(x).remainder(toBigDecimal(y), UNLIMITED);
    }

    public final boolean equalTo(Number x, Number y) {
        return x.equals(toBigDecimal(y));
    }

    public final boolean lessThan(Number x, Number y) {
        return toBigDecimal(x).compareTo(toBigDecimal(y)) < 0;
    }

    public final Number negate(BigDecimal x) {
        return x.negate(UNLIMITED);
    }

    public final Number increment(BigDecimal x) {
        return x.add(ONE, UNLIMITED);
    }

    public final Number decrement(BigDecimal x) {
        return x.subtract(ONE, UNLIMITED);
    }
}

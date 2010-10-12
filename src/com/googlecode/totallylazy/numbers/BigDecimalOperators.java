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

import static com.googlecode.totallylazy.numbers.Numbers.*;
import static java.math.BigDecimal.ONE;
import static java.math.MathContext.UNLIMITED;

final class BigDecimalOperators implements Operators {

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
        return FLOAT_OPS;
    }

    final public Operators opsWith(DoubleOperators x) {
        return DOUBLE_OPS;
    }

    final public Operators opsWith(RatioOperators x) {
        return RATIO_OPS;
    }

    final public Operators opsWith(BigIntegerOperators x) {
        return this;
    }

    final public Operators opsWith(BigDecimalOperators x) {
        return this;
    }

    public boolean isZero(Number x) {
        return toBigDecimal(x).signum() == 0;
    }

    public boolean isPositive(Number x) {
        return toBigDecimal(x).signum() > 0;
    }

    public boolean isNegative(Number x) {
        return toBigDecimal(x).signum() < 0;
    }

    final public Number add(Number x, Number y) {
        return toBigDecimal(x).add(toBigDecimal(y), UNLIMITED);
    }

    final public Number multiply(Number x, Number y) {
        return toBigDecimal(x).multiply(toBigDecimal(y), UNLIMITED);
    }

    public Number divide(Number x, Number y) {
        return toBigDecimal(x).divide(toBigDecimal(y), UNLIMITED);
    }

    public Number quotient(Number x, Number y) {
        return toBigDecimal(x).divideToIntegralValue(toBigDecimal(y), UNLIMITED);
    }

    public Number remainder(Number x, Number y) {
        return toBigDecimal(x).remainder(toBigDecimal(y), UNLIMITED);
    }

    public boolean equalTo(Number x, Number y) {
        return toBigDecimal(x).equals(toBigDecimal(y));
    }

    public boolean lessThan(Number x, Number y) {
        return toBigDecimal(x).compareTo(toBigDecimal(y)) < 0;
    }

    final public Number negate(Number x) {
        return toBigDecimal(x).negate(UNLIMITED);
    }

    public Number increment(Number x) {
        return toBigDecimal(x).add(ONE, UNLIMITED);
    }

    public Number decrement(Number x) {
        return toBigDecimal(x).subtract(ONE, UNLIMITED);
    }
}

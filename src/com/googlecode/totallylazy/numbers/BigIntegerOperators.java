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

import static com.googlecode.totallylazy.numbers.Numbers.*;
import static java.math.BigInteger.ONE;

public final class BigIntegerOperators implements Operators<BigInteger> {
    public Class<BigInteger> forClass() {
        return BigInteger.class;
    }

    public final Number increment(BigInteger x) {
        return reduce(x.add(ONE));
    }

    public final Number decrement(BigInteger x) {
        return reduce(x.subtract(ONE));
    }

    public final boolean isZero(BigInteger x) {
        return x.signum() == 0;
    }

    public final boolean isPositive(BigInteger x) {
        return x.signum() > 0;
    }

    public final boolean isNegative(BigInteger x) {
        return x.signum() < 0;
    }

    public final boolean equalTo(Number x, Number y) {
        return toBigInteger(x).equals(toBigInteger(y));
    }

    public final boolean lessThan(Number x, Number y) {
        return toBigInteger(x).compareTo(toBigInteger(y)) < 0;
    }

    public final Number negate(BigInteger x) {
        return x.negate();
    }

    public final Number add(Number x, Number y) {
        return reduce(toBigInteger(x).add(toBigInteger(y)));
    }

    public final Number multiply(Number x, Number y) {
        return reduce(toBigInteger(x).multiply(toBigInteger(y)));
    }

    public final Number divide(Number x, Number y) {
        return Numbers.divide(toBigInteger(x), toBigInteger(y));
    }

    public final Number quotient(Number x, Number y) {
        return toBigInteger(x).divide(toBigInteger(y));
    }

    public final Number remainder(Number x, Number y) {
        return toBigInteger(x).remainder(toBigInteger(y));
    }

    public static BigInteger toBigInteger(Number x) {
        if (x instanceof BigInteger)
            return (BigInteger) x;
        else
            return BigInteger.valueOf((x).longValue());
    }

}

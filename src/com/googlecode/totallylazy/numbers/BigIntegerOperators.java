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

import static com.googlecode.totallylazy.numbers.Numbers.reduce;
import static java.math.BigInteger.ONE;
import static java.math.BigInteger.ZERO;
import static java.math.BigInteger.valueOf;

public final class BigIntegerOperators implements Operators<BigInteger> {
    public Class<BigInteger> forClass() {
        return BigInteger.class;
    }

    public final Number increment(BigInteger value) {
        return reduce(value.add(ONE));
    }

    public final Number decrement(BigInteger value) {
        return reduce(value.subtract(ONE));
    }

    public final boolean isZero(BigInteger value) {
        return value.signum() == 0;
    }

    public final boolean isPositive(BigInteger value) {
        return value.signum() > 0;
    }

    public final boolean isNegative(BigInteger value) {
        return value.signum() < 0;
    }

    public final boolean equalTo(Number x, Number y) {
        return toBigInteger(x).equals(toBigInteger(y));
    }

    public final boolean lessThan(Number x, Number y) {
        return toBigInteger(x).compareTo(toBigInteger(y)) < 0;
    }

    public final Number negate(BigInteger value) {
        return value.negate();
    }

    public final Number add(Number x, Number y) {
        return reduce(toBigInteger(x).add(toBigInteger(y)));
    }

    public final Number multiply(Number x, Number y) {
        return reduce(toBigInteger(x).multiply(toBigInteger(y)));
    }

    public final Number divide(Number x, Number y) {
        BigInteger n = toBigInteger(x);
        BigInteger d = toBigInteger(y);

        if (d.equals(ZERO)) {
            throw new ArithmeticException("Divide by zero");
        }

        BigInteger gcd = n.gcd(d);

        if (gcd.equals(ZERO)) {
            return 0;
        }

        n = n.divide(gcd);
        d = d.divide(gcd);

        if (d.equals(BigInteger.ONE)) {
            return reduce(n);
        }

        if (d.equals(BigInteger.ONE.negate())) {
            return reduce(n.negate());
        }

        return new Ratio((d.signum() < 0 ? n.negate() : n), (d.signum() < 0 ? d.negate() : d));
    }

    public final Number quotient(Number x, Number y) {
        return toBigInteger(x).divide(toBigInteger(y));
    }

    public final Number remainder(Number x, Number y) {
        return toBigInteger(x).remainder(toBigInteger(y));
    }

    public static BigInteger toBigInteger(Number value) {
        if (value instanceof BigInteger) {
            return (BigInteger) value;
        }
        return valueOf(value.longValue());
    }

}

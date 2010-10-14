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
import static java.lang.Long.MAX_VALUE;
import static java.lang.Long.MIN_VALUE;
import static java.math.BigInteger.valueOf;

public final class LongOperators implements Operators<Long> {
    public Class<Long> forClass() {
        return Long.class;
    }

    public final Number increment(Long value) {
        if (value < MAX_VALUE)
            return value + 1;
        return operatorsFor(BigInteger.class).increment(valueOf(value));
    }

    public final Number decrement(Long value) {
        if (value > MIN_VALUE)
            return value - 1;
        return operatorsFor(BigInteger.class).decrement(valueOf(value));
    }

    public final Number negate(Long value) {
        if (value > MIN_VALUE)
            return -value;
        return valueOf(value).negate();
    }

    public final boolean isZero(Long value) {
        return value == 0;
    }

    public final boolean isPositive(Long value) {
        return value > 0;
    }

    public final boolean isNegative(Long value) {
        return value < 0;
    }

    public final boolean equalTo(Number x, Number y) {
        return x.longValue() == y.longValue();
    }

    public final boolean lessThan(Number x, Number y) {
        return x.longValue() < y.longValue();
    }

    public final Number add(Number x, Number y) {
        long lx = x.longValue(), ly = y.longValue();
        long ret = lx + ly;
        if ((ret ^ lx) < 0 && (ret ^ ly) < 0)
            return operatorsFor(BigInteger.class).add(x, y);
        return ret;
    }

    public final Number multiply(Number x, Number y) {
        long lx = x.longValue(), ly = y.longValue();
        long ret = lx * ly;
        if (ly != 0 && ret / ly != lx)
            return operatorsFor(BigInteger.class).multiply(x, y);
        return ret;
    }

    public static long gcd(long u, long v) {
        while (v != 0) {
            long r = u % v;
            u = v;
            v = r;
        }
        return u;
    }

    public final Number divide(Number x, Number y) {
        long n = x.longValue();
        long val = y.longValue();
        long gcd = gcd(n, val);
        if (gcd == 0)
            return 0;

        n = n / gcd;
        long d = val / gcd;
        if (d == 1)
            return n;
        if (d < 0) {
            n = -n;
            d = -d;
        }
        return new Ratio(valueOf(n), valueOf(d));
    }

    public final Number quotient(Number x, Number y) {
        return x.longValue() / y.longValue();
    }

    public final Number remainder(Number x, Number y) {
        return x.longValue() % y.longValue();
    }

    public static Number reduce(long value) {
        if (value >= Integer.MIN_VALUE && value <= Integer.MAX_VALUE)
            return (int) value;
        else
            return value;
    }
    
}

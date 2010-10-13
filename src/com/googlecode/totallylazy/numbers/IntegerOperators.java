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

import static java.lang.Integer.MAX_VALUE;
import static java.lang.Integer.MIN_VALUE;
import static java.math.BigInteger.valueOf;

public final class IntegerOperators implements Operators<Integer> {
    public Class<Integer> forClass() {
        return Integer.class;
    }

    public final Number negate(Integer val) {
        if (val > MIN_VALUE)
            return -val;
        return -((long) val);
    }

    public final Number increment(Integer val) {
        if (val < MAX_VALUE)
            return val + 1;
        return (long) val + 1;
    }

    public final Number decrement(Integer val) {
        if (val > MIN_VALUE)
            return val - 1;
        return (long) val - 1;
    }

    public final boolean isZero(Integer x) {
        return x == 0;
    }

    public final boolean isPositive(Integer x) {
        return x > 0;
    }

    public final boolean isNegative(Integer x) {
        return x < 0;
    }

    public final boolean equalTo(Number x, Number y) {
        return x.intValue() == y.intValue();
    }

    public final boolean lessThan(Number x, Number y) {
        return x.intValue() < y.intValue();
    }

    public final Number add(Number x, Number y) {
        long ret = x.longValue() + y.longValue();
        if (ret <= MAX_VALUE && ret >= MIN_VALUE)
            return (int) ret;
        return ret;
    }

    public final Number multiply(Number x, Number y) {
        long ret = x.longValue() * y.longValue();
        if (ret <= MAX_VALUE && ret >= MIN_VALUE)
            return (int) ret;
        return ret;
    }

    public static int gcd(int u, int v) {
        while (v != 0) {
            int r = u % v;
            u = v;
            v = r;
        }
        return u;
    }

    public final Number divide(Number x, Number y) {
        int n = x.intValue();
        int val = y.intValue();
        int gcd = gcd(n, val);
        if (gcd == 0)
            return 0;

        n = n / gcd;
        int d = val / gcd;
        if (d == 1)
            return n;
        if (d < 0) {
            n = -n;
            d = -d;
        }
        return new Ratio(valueOf(n), valueOf(d));
    }

    public final Number quotient(Number x, Number y) {
        return x.intValue() / y.intValue();
    }

    public final Number remainder(Number x, Number y) {
        return x.intValue() % y.intValue();
    }
}

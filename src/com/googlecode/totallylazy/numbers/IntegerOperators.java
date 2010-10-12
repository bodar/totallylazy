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
import static com.googlecode.totallylazy.numbers.Numbers.BIGDECIMAL_OPS;
import static com.googlecode.totallylazy.numbers.Numbers.BIGINTEGER_OPS;
import static java.lang.Integer.MAX_VALUE;
import static java.lang.Integer.MIN_VALUE;
import static java.math.BigInteger.valueOf;

final class IntegerOperators implements Operators {
    public Operators combine(Operators y) {
        return y.opsWith(this);
    }

    final public Operators opsWith(IntegerOperators x) {
        return this;
    }

    final public Operators opsWith(LongOperators x) {
        return LONG_OPS;
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
        return BIGINTEGER_OPS;
    }

    final public Operators opsWith(BigDecimalOperators x) {
        return BIGDECIMAL_OPS;
    }

    public boolean isZero(Number x) {
        return x.intValue() == 0;
    }

    public boolean isPositive(Number x) {
        return x.intValue() > 0;
    }

    public boolean isNegative(Number x) {
        return x.intValue() < 0;
    }

    final public Number add(Number x, Number y) {
        long ret = x.longValue() + y.longValue();
        if (ret <= MAX_VALUE && ret >= MIN_VALUE)
            return (int) ret;
        return ret;
    }

    final public Number multiply(Number x, Number y) {
        long ret = x.longValue() * y.longValue();
        if (ret <= MAX_VALUE && ret >= MIN_VALUE)
            return (int) ret;
        return ret;
    }

    static int gcd(int u, int v) {
        while (v != 0) {
            int r = u % v;
            u = v;
            v = r;
        }
        return u;
    }

    public Number divide(Number x, Number y) {
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

    public Number quotient(Number x, Number y) {
        return x.intValue() / y.intValue();
    }

    public Number remainder(Number x, Number y) {
        return x.intValue() % y.intValue();
    }

    public boolean equalTo(Number x, Number y) {
        return x.intValue() == y.intValue();
    }

    public boolean lessThan(Number x, Number y) {
        return x.intValue() < y.intValue();
    }

    final public Number negate(Number x) {
        int val = x.intValue();
        if (val > MIN_VALUE)
            return -val;
        return -((long) val);
    }

    public Number increment(Number x) {
        int val = x.intValue();
        if (val < MAX_VALUE)
            return val + 1;
        return (long) val + 1;
    }

    public Number decrement(Number x) {
        int val = x.intValue();
        if (val > MIN_VALUE)
            return val - 1;
        return (long) val - 1;
    }
}

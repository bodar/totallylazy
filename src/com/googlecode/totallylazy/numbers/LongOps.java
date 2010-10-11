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
import static com.googlecode.totallylazy.numbers.Numbers.BIGDECIMAL_OPS;
import static java.lang.Long.MAX_VALUE;
import static java.lang.Long.MIN_VALUE;
import static java.math.BigInteger.valueOf;

final class LongOps implements Ops {
    public Ops combine(Ops y) {
        return y.opsWith(this);
    }

    final public Ops opsWith(IntegerOps x) {
        return this;
    }

    final public Ops opsWith(LongOps x) {
        return this;
    }

    final public Ops opsWith(FloatOps x) {
        return FLOAT_OPS;
    }

    final public Ops opsWith(DoubleOps x) {
        return DOUBLE_OPS;
    }

    final public Ops opsWith(RatioOps x) {
        return RATIO_OPS;
    }

    final public Ops opsWith(BigIntegerOps x) {
        return BIGINTEGER_OPS;
    }

    final public Ops opsWith(BigDecimalOps x) {
        return BIGDECIMAL_OPS;
    }

    public boolean isZero(Number x) {
        return x.longValue() == 0;
    }

    public boolean isPositive(Number x) {
        return x.longValue() > 0;
    }

    public boolean isNegative(Number x) {
        return x.longValue() < 0;
    }

    final public Number add(Number x, Number y) {
        long lx = x.longValue(), ly = y.longValue();
        long ret = lx + ly;
        if ((ret ^ lx) < 0 && (ret ^ ly) < 0)
            return BIGINTEGER_OPS.add(x, y);
        return ret;
    }

    final public Number multiply(Number x, Number y) {
        long lx = x.longValue(), ly = y.longValue();
        long ret = lx * ly;
        if (ly != 0 && ret / ly != lx)
            return BIGINTEGER_OPS.multiply(x, y);
        return ret;
    }

    static long gcd(long u, long v) {
        while (v != 0) {
            long r = u % v;
            u = v;
            v = r;
        }
        return u;
    }

    public Number divide(Number x, Number y) {
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

    public Number quotient(Number x, Number y) {
        return x.longValue() / y.longValue();
    }

    public Number remainder(Number x, Number y) {
        return x.longValue() % y.longValue();
    }

    public boolean equalTo(Number x, Number y) {
        return x.longValue() == y.longValue();
    }

    public boolean lessThan(Number x, Number y) {
        return x.longValue() < y.longValue();
    }

    //public Number subtract(Number x, Number y);
    final public Number negate(Number x) {
        long val = x.longValue();
        if (val > MIN_VALUE)
            return -val;
        return valueOf(val).negate();
    }

    public Number increment(Number x) {
        long val = x.longValue();
        if (val < MAX_VALUE)
            return val + 1;
        return BIGINTEGER_OPS.increment(x);
    }

    public Number decrement(Number x) {
        long val = x.longValue();
        if (val > MIN_VALUE)
            return val - 1;
        return BIGINTEGER_OPS.decrement(x);
    }
}

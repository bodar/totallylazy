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
        return Numbers.FLOAT_OPS;
    }

    final public Ops opsWith(DoubleOps x) {
        return Numbers.DOUBLE_OPS;
    }

    final public Ops opsWith(RatioOps x) {
        return Numbers.RATIO_OPS;
    }

    final public Ops opsWith(BigIntegerOps x) {
        return Numbers.BIGINTEGER_OPS;
    }

    final public Ops opsWith(BigDecimalOps x) {
        return Numbers.BIGDECIMAL_OPS;
    }

    public boolean isZero(Number x) {
        return x.longValue() == 0;
    }

    public boolean isPos(Number x) {
        return x.longValue() > 0;
    }

    public boolean isNeg(Number x) {
        return x.longValue() < 0;
    }

    final public Number add(Number x, Number y) {
        long lx = x.longValue(), ly = y.longValue();
        long ret = lx + ly;
        if ((ret ^ lx) < 0 && (ret ^ ly) < 0)
            return Numbers.BIGINTEGER_OPS.add(x, y);
        return ret;
    }

    final public Number multiply(Number x, Number y) {
        long lx = x.longValue(), ly = y.longValue();
        long ret = lx * ly;
        if (ly != 0 && ret / ly != lx)
            return Numbers.BIGINTEGER_OPS.multiply(x, y);
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
        return new Ratio(BigInteger.valueOf(n), BigInteger.valueOf(d));
    }

    public Number quotient(Number x, Number y) {
        return x.longValue() / y.longValue();
    }

    public Number remainder(Number x, Number y) {
        return x.longValue() % y.longValue();
    }

    public boolean equiv(Number x, Number y) {
        return x.longValue() == y.longValue();
    }

    public boolean lt(Number x, Number y) {
        return x.longValue() < y.longValue();
    }

    //public Number subtract(Number x, Number y);
    final public Number negate(Number x) {
        long val = x.longValue();
        if (val > Long.MIN_VALUE)
            return -val;
        return BigInteger.valueOf(val).negate();
    }

    public Number inc(Number x) {
        long val = x.longValue();
        if (val < Long.MAX_VALUE)
            return val + 1;
        return Numbers.BIGINTEGER_OPS.inc(x);
    }

    public Number dec(Number x) {
        long val = x.longValue();
        if (val > Long.MIN_VALUE)
            return val - 1;
        return Numbers.BIGINTEGER_OPS.dec(x);
    }
}

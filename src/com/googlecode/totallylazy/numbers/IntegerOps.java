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

final class IntegerOps implements Ops {
    public Ops combine(Ops y) {
        return y.opsWith(this);
    }

    final public Ops opsWith(IntegerOps x) {
        return this;
    }

    final public Ops opsWith(LongOps x) {
        return Numbers.LONG_OPS;
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
        return x.intValue() == 0;
    }

    public boolean isPos(Number x) {
        return x.intValue() > 0;
    }

    public boolean isNeg(Number x) {
        return x.intValue() < 0;
    }

    final public Number add(Number x, Number y) {
        long ret = x.longValue() + y.longValue();
        if (ret <= Integer.MAX_VALUE && ret >= Integer.MIN_VALUE)
            return (int) ret;
        return ret;
    }

    final public Number multiply(Number x, Number y) {
        long ret = x.longValue() * y.longValue();
        if (ret <= Integer.MAX_VALUE && ret >= Integer.MIN_VALUE)
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
        return new Ratio(BigInteger.valueOf(n), BigInteger.valueOf(d));
    }

    public Number quotient(Number x, Number y) {
        return x.intValue() / y.intValue();
    }

    public Number remainder(Number x, Number y) {
        return x.intValue() % y.intValue();
    }

    public boolean equiv(Number x, Number y) {
        return x.intValue() == y.intValue();
    }

    public boolean lt(Number x, Number y) {
        return x.intValue() < y.intValue();
    }

    //public Number subtract(Number x, Number y);
    final public Number negate(Number x) {
        int val = x.intValue();
        if (val > Integer.MIN_VALUE)
            return -val;
        return -((long) val);
    }

    public Number inc(Number x) {
        int val = x.intValue();
        if (val < Integer.MAX_VALUE)
            return val + 1;
        return (long) val + 1;
    }

    public Number dec(Number x) {
        int val = x.intValue();
        if (val > Integer.MIN_VALUE)
            return val - 1;
        return (long) val - 1;
    }
}

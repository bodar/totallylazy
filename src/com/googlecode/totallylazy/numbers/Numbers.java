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

package com.googlecode.totallylazy.numbers;

import java.math.BigDecimal;
import java.math.BigInteger;

public class Numbers {
    static public boolean isZero(Number x) {
        return ops(x).isZero(x);
    }

    static public boolean isPositive(Number x) {
        return ops(x).isPositive(x);
    }

    static public boolean isNegative(Number x) {
        return ops(x).isNegative(x);
    }

    static public Number negate(Number x) {
        return ops(x).negate(x);
    }

    static public Number increment(Number x) {
        return ops(x).increment(x);
    }

    static public Number decrement(Number x) {
        return ops(x).decrement(x);
    }

    static public Number add(Number x, Number y) {
        return ops(x).combine(ops(y)).add(x, y);
    }

    static public Number subtract(Number x, Number y) {
        Ops yops = ops(y);
        return ops(x).combine(yops).add(x, yops.negate(y));
    }

    static public Number multiply(Number x, Number y) {
        return ops(x).combine(ops(y)).multiply(x, y);
    }

    static public Number divide(Number x, Number y) {
        Ops yops = ops(y);
        if (yops.isZero(y))
            throw new ArithmeticException("Divide by zero");
        return ops(x).combine(yops).divide(x, y);
    }

    static public Number quotient(Number x, Number y) {
        Ops yops = ops(y);
        if (yops.isZero(y))
            throw new ArithmeticException("Divide by zero");
        return reduce(ops(x).combine(yops).quotient(x, y));
    }

    static public Number remainder(Number x, Number y) {
        Ops yops = ops(y);
        if (yops.isZero(y))
            throw new ArithmeticException("Divide by zero");
        return reduce(ops(x).combine(yops).remainder(x, y));
    }

    static Number quotient(double n, double d) {
        double q = n / d;
        if (q <= Integer.MAX_VALUE && q >= Integer.MIN_VALUE) {
            return (int) q;
        } else { //bigint quotient
            return reduce(new BigDecimal(q).toBigInteger());
        }
    }

    static Number remainder(double n, double d) {
        double q = n / d;
        if (q <= Integer.MAX_VALUE && q >= Integer.MIN_VALUE) {
            return (n - ((int) q) * d);
        } else { //bigint quotient
            Number bq = reduce(new BigDecimal(q).toBigInteger());
            return (n - bq.doubleValue() * d);
        }
    }

    static public boolean equiv(Number x, Number y) {
        return ops(x).combine(ops(y)).equalTo(x, y);
    }

    static public boolean lt(Number x, Number y) {
        return ops(x).combine(ops(y)).lessThan(x, y);
    }

    static public boolean lte(Number x, Number y) {
        return !ops(x).combine(ops(y)).lessThan(y, x);
    }

    static public boolean gt(Number x, Number y) {
        return ops(x).combine(ops(y)).lessThan(y, x);
    }

    static public boolean gte(Number x, Number y) {
        return !ops(x).combine(ops(y)).lessThan(x, y);
    }

    static public int compare(Number x, Number y) {
        Ops ops = ops(x).combine(ops(y));
        if (ops.lessThan(x, y))
            return -1;
        else if (ops.lessThan(y, x))
            return 1;
        return 0;
    }

    static BigInteger toBigInteger(Number x) {
        if (x instanceof BigInteger)
            return (BigInteger) x;
        else
            return BigInteger.valueOf((x).longValue());
    }

    static BigDecimal toBigDecimal(Number x) {
        if (x instanceof BigDecimal)
            return (BigDecimal) x;
        else if (x instanceof BigInteger)
            return new BigDecimal((BigInteger) x);
        else
            return BigDecimal.valueOf((x).longValue());
    }

    static Ratio toRatio(Number x) {
        if (x instanceof Ratio)
            return (Ratio) x;
        else if (x instanceof BigDecimal) {
            BigDecimal bx = (BigDecimal) x;
            BigInteger bv = bx.unscaledValue();
            int scale = bx.scale();
            if (scale < 0)
                return new Ratio(bv.multiply(BigInteger.TEN.pow(-scale)), BigInteger.ONE);
            else
                return new Ratio(bv, BigInteger.TEN.pow(scale));
        }
        return new Ratio(toBigInteger(x), BigInteger.ONE);
    }

    static public Number rationalize(Number x) {
        if (x instanceof Float || x instanceof Double)
            return rationalize(BigDecimal.valueOf(x.doubleValue()));
        else if (x instanceof BigDecimal) {
            BigDecimal bx = (BigDecimal) x;
            BigInteger bv = bx.unscaledValue();
            int scale = bx.scale();
            if (scale < 0)
                return bv.multiply(BigInteger.TEN.pow(-scale));
            else
                return divide(bv, BigInteger.TEN.pow(scale));
        }
        return x;
    }

    static public Number reduce(Number val) {
        if (val instanceof Long)
            return reduce(val.longValue());
        else if (val instanceof BigInteger)
            return reduce((BigInteger) val);
        return val;
    }

    static public Number reduce(BigInteger val) {
        int bitLength = val.bitLength();
        if (bitLength < 32)
            return val.intValue();
        else if (bitLength < 64)
            return val.longValue();
        else
            return val;
    }

    static public Number reduce(long val) {
        if (val >= Integer.MIN_VALUE && val <= Integer.MAX_VALUE)
            return (int) val;
        else
            return val;
    }

    static public Number divide(BigInteger n, BigInteger d) {
        if (d.equals(BigInteger.ZERO))
            throw new ArithmeticException("Divide by zero");
        BigInteger gcd = n.gcd(d);
        if (gcd.equals(BigInteger.ZERO))
            return 0;
        n = n.divide(gcd);
        d = d.divide(gcd);
        if (d.equals(BigInteger.ONE))
            return reduce(n);
        else if (d.equals(BigInteger.ONE.negate()))
            return reduce(n.negate());
        return new Ratio((d.signum() < 0 ? n.negate() : n),
                (d.signum() < 0 ? d.negate() : d));
    }

    static public Number not(Number x) {
        return bitOps(x).not(x);
    }


    static public Number and(Number x, Number y) {
        return bitOps(x).combine(bitOps(y)).and(x, y);
    }

    static public Number or(Number x, Number y) {
        return bitOps(x).combine(bitOps(y)).or(x, y);
    }

    static public Number xor(Number x, Number y) {
        return bitOps(x).combine(bitOps(y)).xor(x, y);
    }

    static public Number andNot(Number x, Number y) {
        return bitOps(x).combine(bitOps(y)).andNot(x, y);
    }

    static public Number clearBit(Number x, int n) {
        if (n < 0)
            throw new ArithmeticException("Negative bit index");
        return bitOps(x).clearBit(x, n);
    }

    static public Number setBit(Number x, int n) {
        if (n < 0)
            throw new ArithmeticException("Negative bit index");
        return bitOps(x).setBit(x, n);
    }

    static public Number flipBit(Number x, int n) {
        if (n < 0)
            throw new ArithmeticException("Negative bit index");
        return bitOps(x).flipBit(x, n);
    }

    static public boolean testBit(Number x, int n) {
        if (n < 0)
            throw new ArithmeticException("Negative bit index");
        return bitOps(x).testBit(x, n);
    }

    static public Number shiftLeft(Number x, Number n) {
        return bitOps(x).shiftLeft(x, (n).intValue());
    }

    static public int shiftLeft(int x, int n) {
        return x << n;
    }

    static public Number shiftRight(Number x, Number n) {
        return bitOps(x).shiftRight(x, (n).intValue());
    }

    static public int shiftRight(int x, int n) {
        return x >> n;
    }

    static final IntegerOps INTEGER_OPS = new IntegerOps();
    static final LongOps LONG_OPS = new LongOps();
    static final FloatOps FLOAT_OPS = new FloatOps();
    static final DoubleOps DOUBLE_OPS = new DoubleOps();
    static final RatioOps RATIO_OPS = new RatioOps();
    static final BigIntegerOps BIGINTEGER_OPS = new BigIntegerOps();
    static final BigDecimalOps BIGDECIMAL_OPS = new BigDecimalOps();

    static final IntegerBitOps INTEGER_BITOPS = new IntegerBitOps();
    static final LongBitOps LONG_BITOPS = new LongBitOps();
    static final BigIntegerBitOps BIGINTEGER_BITOPS = new BigIntegerBitOps();

    static Ops ops(Number x) {
        Class xc = x.getClass();

        if (xc == Integer.class)
            return INTEGER_OPS;
        else if (xc == Double.class)
            return DOUBLE_OPS;
        else if (xc == Float.class)
            return FLOAT_OPS;
        else if (xc == BigInteger.class)
            return BIGINTEGER_OPS;
        else if (xc == Long.class)
            return LONG_OPS;
        else if (xc == Ratio.class)
            return RATIO_OPS;
        else if (xc == BigDecimal.class)
            return BIGDECIMAL_OPS;
        else
            return INTEGER_OPS;
    }

    static BitOps bitOps(Number x) {
        Class xc = x.getClass();

        if (xc == Integer.class)
            return INTEGER_BITOPS;
        else if (xc == Long.class)
            return LONG_BITOPS;
        else if (xc == BigInteger.class)
            return BIGINTEGER_BITOPS;
        else if (xc == Double.class || xc == Float.class || xc == BigDecimalOps.class || xc == Ratio.class)
            throw new ArithmeticException("bit operation on non integer type: " + xc);
        else
            return INTEGER_BITOPS;
    }

}

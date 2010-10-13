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
    public static <T extends Number> boolean isZero(T x) {
        return with(x).isZero(x);
    }

    static public boolean isPositive(Number x) {
        return with(x).isPositive(x);
    }

    static public boolean isNegative(Number x) {
        return with(x).isNegative(x);
    }

    static public Number negate(Number x) {
        return with(x).negate(x);
    }

    static public Number increment(Number x) {
        return with(x).increment(x);
    }

    static public Number decrement(Number x) {
        return with(x).decrement(x);
    }

    static public Number add(Number x, Number y) {
        return with(x).combine(with(y)).add(x, y);
    }

    static public Number subtract(Number x, Number y) {
        Operators yops = with(y);
        return with(x).combine(yops).add(x, yops.negate(y));
    }

    static public Number multiply(Number x, Number y) {
        return with(x).combine(with(y)).multiply(x, y);
    }

    static public Number divide(Number x, Number y) {
        Operators yops = with(y);
        if (yops.isZero(y))
            throw new ArithmeticException("Divide by zero");
        return with(x).combine(yops).divide(x, y);
    }

    static public Number quotient(Number x, Number y) {
        Operators yops = with(y);
        if (yops.isZero(y))
            throw new ArithmeticException("Divide by zero");
        return reduce(with(x).combine(yops).quotient(x, y));
    }

    static public Number remainder(Number x, Number y) {
        Operators yops = with(y);
        if (yops.isZero(y))
            throw new ArithmeticException("Divide by zero");
        return reduce(with(x).combine(yops).remainder(x, y));
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

    static public boolean equalTo(Number x, Number y) {
        return with(x).combine(with(y)).equalTo(x, y);
    } 

    static public boolean lessThan(Number x, Number y) {
        return with(x).combine(with(y)).lessThan(x, y);
    }

    static public boolean lessThanOrEqual(Number x, Number y) {
        return !with(x).combine(with(y)).lessThan(y, x);
    }

    static public boolean greaterThan(Number x, Number y) {
        return with(x).combine(with(y)).lessThan(y, x);
    }

    static public boolean greaterThanOrEqual(Number x, Number y) {
        return !with(x).combine(with(y)).lessThan(x, y);
    }

    static public int compare(Number x, Number y) {
        Operators operators = with(x).combine(with(y));
        if (operators.lessThan(x, y))
            return -1;
        else if (operators.lessThan(y, x))
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

    static final IntegerOperators INTEGER_OPS = new IntegerOperators();
    static final LongOperators LONG_OPS = new LongOperators();
    static final FloatOperators FLOAT_OPS = new FloatOperators();
    static final DoubleOperators DOUBLE_OPS = new DoubleOperators();
    static final RatioOperators RATIO_OPS = new RatioOperators();
    static final BigIntegerOperators BIGINTEGER_OPS = new BigIntegerOperators();
    static final BigDecimalOperators BIGDECIMAL_OPS = new BigDecimalOperators();

    static final IntegerBitOperators INTEGER_BITOPS = new IntegerBitOperators();
    static final LongBitOperators LONG_BITOPS = new LongBitOperators();
    static final BigIntegerBitOperators BIGINTEGER_BITOPS = new BigIntegerBitOperators();

    static Operators with(Number x) {
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

    static BitOperators bitOps(Number x) {
        Class xc = x.getClass();

        if (xc == Integer.class)
            return INTEGER_BITOPS;
        else if (xc == Long.class)
            return LONG_BITOPS;
        else if (xc == BigInteger.class)
            return BIGINTEGER_BITOPS;
        else if (xc == Double.class || xc == Float.class || xc == BigDecimalOperators.class || xc == Ratio.class)
            throw new ArithmeticException("bit operation on non integer type: " + xc);
        else
            return INTEGER_BITOPS;
    }

}

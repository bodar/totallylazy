/*
This code is a a heavily modified version of Numbers from Rich Hickeys clojure core, see Numbers.java for copyright
 */

package com.googlecode.totallylazy.numbers;

import java.math.BigInteger;

import static java.math.BigInteger.ONE;
import static java.math.BigInteger.ZERO;
import static java.math.BigInteger.valueOf;

public final class BigIntegerOperators implements Operators<BigInteger>, IntegralOperators {
    public static BigIntegerOperators Instance = new BigIntegerOperators();

    private BigIntegerOperators() {}

    public final Class<BigInteger> forClass() {
        return BigInteger.class;
    }

    public final int priority() {
        return 3;
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
        return value.signum() == 1;
    }

    public final boolean isNegative(BigInteger value) {
        return value.signum() == -1;
    }

    public final boolean equalTo(Number x, Number y) {
        return bigInteger(x).equals(bigInteger(y));
    }

    public final boolean lessThan(Number x, Number y) {
        return bigInteger(x).compareTo(bigInteger(y)) < 0;
    }

    @Override
    public Number absolute(BigInteger value) {
        return value.abs();
    }

    public final Number negate(BigInteger value) {
        return value.negate();
    }

    public final Number add(Number x, Number y) {
        return reduce(bigInteger(x).add(bigInteger(y)));
    }

    public final Number multiply(Number x, Number y) {
        return reduce(bigInteger(x).multiply(bigInteger(y)));
    }

    public final Number divide(Number x, Number y) {
       return divide(bigInteger(x), bigInteger(y));
    }

    public static Number divide(BigInteger n, BigInteger d) {
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

    public static Number reduce(BigInteger value) {
        int bitLength = value.bitLength();
        if (bitLength < 32)
            return value.intValue();
        else if (bitLength < 64)
            return value.longValue();
        else
            return value;
    }

    public final Number quotient(Number x, Number y) {
        BigInteger bigInteger = bigInteger(x);
        BigInteger val = bigInteger(y);
        return quotient(bigInteger, val);
    }

    public static BigInteger quotient(BigInteger x, BigInteger y) {
        return x.divide(y);
    }

    public final Number remainder(Number x, Number y) {
        return bigInteger(x).remainder(bigInteger(y));
    }

    @Override
    public Number gcd(Number x, Number y) {
        return gcd(bigInteger(x), bigInteger(y));
    }

    public static BigInteger gcd(BigInteger bigInteger, BigInteger val) {
        return bigInteger.gcd(val);
    }

    @Override
    public Number lcm(Number x, Number y) {
        return lcm(bigInteger(x), bigInteger(y));
    }

    public static BigInteger lcm(BigInteger x, BigInteger y) {
        if(x.signum() == 0 || y.signum() == 0) return ZERO;
        return y.multiply(quotient(x, gcd(x, y))).abs();
    }

    public static BigInteger bigInteger(Number value) {
        if (value instanceof BigInteger) {
            return (BigInteger) value;
        }
        return valueOf(value.longValue());
    }

}

/*
This code is a a heavily modified version of Numbers from Rich Hickeys clojure core, see Numbers.java for copyright
 */

package com.googlecode.totallylazy.numbers;

import com.googlecode.totallylazy.annotations.tailrec;

import static java.lang.Integer.MAX_VALUE;
import static java.lang.Integer.MIN_VALUE;
import static java.math.BigInteger.valueOf;

public final class IntegerOperators implements Operators<Integer>, IntegralOperators {
    public static IntegerOperators Instance = new IntegerOperators();

    private IntegerOperators() {}

    public final Class<Integer> forClass() {
        return Integer.class;
    }

    public final int priority() {
        return 0;
    }

    public final Number negate(Integer value) {
        if (value > MIN_VALUE)
            return -value;
        return -((long) value);
    }

    public final Number increment(Integer value) {
        if (value < MAX_VALUE)
            return value + 1;
        return (long) value + 1;
    }

    public final Number decrement(Integer value) {
        if (value > MIN_VALUE)
            return value - 1;
        return (long) value - 1;
    }

    public final boolean isZero(Integer value) {
        return value == 0;
    }

    public final boolean isPositive(Integer value) {
        return value > 0;
    }

    public final boolean isNegative(Integer value) {
        return value < 0;
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

    @Override
    public final Number gcd(Number x, Number y) {
        return gcd(x.intValue(), y.intValue());
    }

    public Number lcm(Number x, Number y) {
        return lcm(x.intValue(), y.intValue());
    }

    public static int lcm(int x, int y) {
        if(x == 0 || y == 0) return 0;
        return Math.abs(y * quotient(x, gcd(x, y)));
    }

    @tailrec
    public static int gcd(int x, int y) {
        if (y == 0) return x;
        return gcd(y, x % y);
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
        return quotient(x.intValue(), y.intValue());
    }

    public static int quotient(int x, int y) {
        return x / y;
    }

    public final Number remainder(Number x, Number y) {
        return x.intValue() % y.intValue();
    }
}

/*
This code is a a heavily modified version of Numbers from Rich Hickeys clojure core, see Numbers.java for copyright
 */

package com.googlecode.totallylazy.numbers;

import static java.lang.Integer.MAX_VALUE;
import static java.lang.Integer.MIN_VALUE;
import static java.math.BigInteger.valueOf;

public final class IntegerOperators implements Operators<Integer> {
    public Class<Integer> forClass() {
        return Integer.class;
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

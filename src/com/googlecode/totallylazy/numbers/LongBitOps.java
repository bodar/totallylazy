package com.googlecode.totallylazy.numbers;

/**
 * Copyright (c) Rich Hickey. All rights reserved.
 * The use and distribution terms for this software are covered by the
 * Eclipse Public License 1.0 (http://opensource.org/licenses/eclipse-1.0.php)
 * which can be found in the file epl-v10.html at the root of this distribution.
 * By using this software in any fashion, you are agreeing to be bound by
 * the terms of this license.
 * You must not remove this notice, or any other, from this software.
 */

/* rich Mar 31, 2008 */

final class LongBitOps implements BitOps {
    public BitOps combine(BitOps y) {
        return y.bitOpsWith(this);
    }

    final public BitOps bitOpsWith(IntegerBitOps x) {
        return this;
    }

    final public BitOps bitOpsWith(LongBitOps x) {
        return this;
    }

    final public BitOps bitOpsWith(BigIntegerBitOps x) {
        return Numbers.BIGINTEGER_BITOPS;
    }

    public Number not(Number x) {
        return ~x.longValue();
    }

    public Number and(Number x, Number y) {
        return x.longValue() & y.longValue();
    }

    public Number or(Number x, Number y) {
        return x.longValue() | y.longValue();
    }

    public Number xor(Number x, Number y) {
        return x.longValue() ^ y.longValue();
    }

    public Number andNot(Number x, Number y) {
        return x.longValue() & ~y.longValue();
    }

    public Number clearBit(Number x, int n) {
        if (n < 63)
            return x.longValue() & ~(1L << n);
        else
            return Numbers.toBigInteger(x).clearBit(n);
    }

    public Number setBit(Number x, int n) {
        if (n < 63)
            return x.longValue() | (1L << n);
        else
            return Numbers.toBigInteger(x).setBit(n);
    }

    public Number flipBit(Number x, int n) {
        if (n < 63)
            return x.longValue() ^ (1L << n);
        else
            return Numbers.toBigInteger(x).flipBit(n);
    }

    public boolean testBit(Number x, int n) {
        if (n < 64)
            return (x.longValue() & (1L << n)) != 0;
        else
            return Numbers.toBigInteger(x).testBit(n);
    }

    public Number shiftLeft(Number x, int n) {
        if (n < 0)
            return shiftRight(x, -n);
        return Numbers.reduce(Numbers.toBigInteger(x).shiftLeft(n));
    }

    public Number shiftRight(Number x, int n) {
        if (n < 0)
            return shiftLeft(x, -n);
        return x.longValue() >> n;
    }
}
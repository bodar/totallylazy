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

final class IntegerBitOps implements BitOps {
    public BitOps combine(BitOps y) {
        return y.bitOpsWith(this);
    }

    final public BitOps bitOpsWith(IntegerBitOps x) {
        return this;
    }

    final public BitOps bitOpsWith(LongBitOps x) {
        return Numbers.LONG_BITOPS;
    }

    final public BitOps bitOpsWith(BigIntegerBitOps x) {
        return Numbers.BIGINTEGER_BITOPS;
    }


    public Number not(Number x) {
        return ~x.intValue();
    }

    public Number and(Number x, Number y) {
        return x.intValue() & y.intValue();
    }

    public Number or(Number x, Number y) {
        return x.intValue() | y.intValue();
    }

    public Number xor(Number x, Number y) {
        return x.intValue() ^ y.intValue();
    }

    public Number andNot(Number x, Number y) {
        return x.intValue() & ~y.intValue();
    }

    public Number clearBit(Number x, int n) {
        if (n < 31)
            return x.intValue() & ~(1 << n);
        else if (n < 63)
            return x.longValue() & ~(1L << n);
        else
            return Numbers.toBigInteger(x).clearBit(n);
    }

    public Number setBit(Number x, int n) {
        if (n < 31)
            return x.intValue() | (1 << n);
        else if (n < 63)
            return x.longValue() | (1L << n);
        else
            return Numbers.toBigInteger(x).setBit(n);
    }

    public Number flipBit(Number x, int n) {
        if (n < 31)
            return x.intValue() ^ (1 << n);
        else if (n < 63)
            return x.longValue() ^ (1L << n);
        else
            return Numbers.toBigInteger(x).flipBit(n);
    }

    public boolean testBit(Number x, int n) {
        if (n < 32)
            return (x.intValue() & (1 << n)) != 0;
        else if (n < 64)
            return (x.longValue() & (1L << n)) != 0;
        else
            return Numbers.toBigInteger(x).testBit(n);
    }

    public Number shiftLeft(Number x, int n) {
        if (n < 32) {
            if (n < 0)
                return shiftRight(x, -n);
            return Numbers.reduce(x.longValue() << n);
        } else
            return Numbers.reduce(Numbers.toBigInteger(x).shiftLeft(n));
    }

    public Number shiftRight(Number x, int n) {
        if (n < 0)
            return shiftLeft(x, -n);
        return x.intValue() >> n;
    }
}

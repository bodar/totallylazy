package com.googlecode.totallylazy.numbers;

import static com.googlecode.totallylazy.numbers.BigIntegerOperators.toBigInteger;

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

final class LongBitOperators implements BitOperators {
    public BitOperators combine(BitOperators operators) {
        return operators.bitOpsWith(this);
    }

    final public BitOperators bitOpsWith(IntegerBitOperators operators) {
        return this;
    }

    final public BitOperators bitOpsWith(LongBitOperators operators) {
        return this;
    }

    final public BitOperators bitOpsWith(BigIntegerBitOperators operators) {
        return Numbers.BIGINTEGER_BITOPS;
    }

    public Number not(Number value) {
        return ~value.longValue();
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

    public Number clearBit(Number value, int n) {
        if (n < 63)
            return value.longValue() & ~(1L << n);
        else
            return toBigInteger(value).clearBit(n);
    }

    public Number setBit(Number value, int n) {
        if (n < 63)
            return value.longValue() | (1L << n);
        else
            return toBigInteger(value).setBit(n);
    }

    public Number flipBit(Number value, int n) {
        if (n < 63)
            return value.longValue() ^ (1L << n);
        else
            return toBigInteger(value).flipBit(n);
    }

    public boolean testBit(Number value, int n) {
        if (n < 64)
            return (value.longValue() & (1L << n)) != 0;
        else
            return toBigInteger(value).testBit(n);
    }

    public Number shiftLeft(Number value, int n) {
        if (n < 0)
            return shiftRight(value, -n);
        return Numbers.reduce(toBigInteger(value).shiftLeft(n));
    }

    public Number shiftRight(Number value, int n) {
        if (n < 0)
            return shiftLeft(value, -n);
        return value.longValue() >> n;
    }
}

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

public final class BigIntegerBitOperators implements BitOperators {
    public final BitOperators combine(BitOperators y) {
        return y.bitOpsWith(this);
    }

    public final BitOperators bitOpsWith(IntegerBitOperators x) {
        return this;
    }

    public final BitOperators bitOpsWith(LongBitOperators x) {
        return this;
    }

    public final BitOperators bitOpsWith(BigIntegerBitOperators x) {
        return this;
    }

    public final Number not(Number x) {
        return toBigInteger(x).not();
    }

    public final Number and(Number x, Number y) {
        return toBigInteger(x).and(toBigInteger(y));
    }

    public final Number or(Number x, Number y) {
        return toBigInteger(x).or(toBigInteger(y));
    }

    public final Number xor(Number x, Number y) {
        return toBigInteger(x).xor(toBigInteger(y));
    }

    public final Number andNot(Number x, Number y) {
        return toBigInteger(x).andNot(toBigInteger(y));
    }

    public final Number clearBit(Number x, int n) {
        return toBigInteger(x).clearBit(n);
    }

    public final Number setBit(Number x, int n) {
        return toBigInteger(x).setBit(n);
    }

    public final Number flipBit(Number x, int n) {
        return toBigInteger(x).flipBit(n);
    }

    public final boolean testBit(Number x, int n) {
        return toBigInteger(x).testBit(n);
    }

    public final Number shiftLeft(Number x, int n) {
        return toBigInteger(x).shiftLeft(n);
    }

    public final Number shiftRight(Number x, int n) {
        return toBigInteger(x).shiftRight(n);
    }
}
